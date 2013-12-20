
package com.newpecunia.logging;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.HTMLLayout;
import org.apache.logging.log4j.core.net.SMTPManager;

/**
 * Reimplementation of Log4j2 SMTPAppender. 
 * It throttles the frequesncy of emails to maximally one per 15 minutes.
 */
@Plugin(name = "ThrottlingSMTPAppender", category = "Core", elementType = "appender", printObject = true)
public final class ThrottlingSMTPAppender extends AbstractAppender {

	private static final int DEFAULT_THROTTLING_TIMEOUT = 15*60*1000; //15 minutes
	
	private static final int DEFAULT_BUFFER_SIZE = 512;

    /** The SMTP Manager */
    protected final SMTPManager manager;
    
    private int throttlingTimeout;

    private ThrottlingSMTPAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final SMTPManager manager,
                         final boolean ignoreExceptions, final int throttlingTimeout) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
        this.throttlingTimeout = throttlingTimeout;
    }


    @PluginFactory
    public static ThrottlingSMTPAppender createAppender(
            @PluginAttribute("name") final String name,
            @PluginAttribute("to") final String to,
            @PluginAttribute("cc") final String cc,
            @PluginAttribute("bcc") final String bcc,
            @PluginAttribute("from") final String from,
            @PluginAttribute("replyTo") final String replyTo,
            @PluginAttribute("subject") final String subject,
            @PluginAttribute("smtpProtocol") final String smtpProtocol,
            @PluginAttribute("smtpHost") final String smtpHost,
            @PluginAttribute("smtpPort") final String smtpPortStr,
            @PluginAttribute("smtpUsername") final String smtpUsername,
            @PluginAttribute("smtpPassword") final String smtpPassword,
            @PluginAttribute("smtpDebug") final String smtpDebug,
            @PluginAttribute("bufferSize") final String bufferSizeStr,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") Filter filter,
            @PluginAttribute("ignoreExceptions") final String ignore,
    		@PluginAttribute("throttlingTimeout") final String throttlingTimeoutStr) {
        if (name == null) {
            LOGGER.error("No name provided for ThrottlingSMTPAppender");
            return null;
        }

        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final int smtpPort = AbstractAppender.parseInt(smtpPortStr, 0);
        final boolean isSmtpDebug = Boolean.parseBoolean(smtpDebug);
        final int bufferSize = bufferSizeStr == null ? DEFAULT_BUFFER_SIZE : Integer.parseInt(bufferSizeStr);
        final int throttlingTimeout = throttlingTimeoutStr == null ? DEFAULT_THROTTLING_TIMEOUT : Integer.parseInt(throttlingTimeoutStr) * 1000;
        
        if (layout == null) {
            layout = HTMLLayout.createLayout(null, null, null, null, null, null);
        }
        if (filter == null) {
            filter = ThresholdFilter.createFilter(null, null, null);
        }

        final SMTPManager manager = SMTPManager.getSMTPManager(to, cc, bcc, from, replyTo, subject, smtpProtocol,
            smtpHost, smtpPort, smtpUsername, smtpPassword, isSmtpDebug, filter.toString(),  bufferSize);
        if (manager == null) {
            return null;
        }

        return new ThrottlingSMTPAppender(name, filter, layout, manager, ignoreExceptions, throttlingTimeout);
    }

    /**
     * Capture all events in CyclicBuffer.
     * @param event The Log event.
     * @return true if the event should be filtered.
     */
    @Override
    public boolean isFiltered(final LogEvent event) {
        final boolean filtered = super.isFiltered(event);
        if (filtered) {
            manager.add(event);
        }
        return filtered;
    }

    private long lastSentEmailTimestamp = 0;
    
    /**
     * Send e-mail if the last sent email was more than 15 minutes in the history.
     * @param event The Log event.
     */
    @Override
    public void append(final LogEvent event) {
    	long now = System.currentTimeMillis();
    	if (now - lastSentEmailTimestamp > throttlingTimeout) {
    		manager.sendEvents(getLayout(), event);
    		lastSentEmailTimestamp = now;
    	}
    }
}
