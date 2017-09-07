package com.ghacupha.paycal.config.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fctory for utilities
 * <p>
 * Created by edwin.njeru on 8/23/17.
 */
public class UtilFactory {

    private static final Logger log = LoggerFactory.getLogger(UtilFactory.class);
    private static UtilFactory instance = new UtilFactory();

    private UtilFactory() {

        log.debug("Instantiating the utility factory");
    }

    public static UtilFactory getInstance() {

        log.debug("Returning utility factory singleton instance");
        return instance;
    }
}
