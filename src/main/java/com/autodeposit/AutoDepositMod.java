package com.autodeposit;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoDepositMod implements ModInitializer {
    public static final String MOD_ID = "autodeposit";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModConfig config = ModConfig.load();
        DepositHelper.updateTargetItems(config);
        DepositTickHandler.register();
        LOGGER.info("AutoDeposit mod initialized for 1.20.4");
    }
          }
