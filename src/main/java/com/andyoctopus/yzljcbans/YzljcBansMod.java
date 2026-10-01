package com.andyoctopus.yzljcbans;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
        modid = YzljcBansMod.MODID,
        name = YzljcBansMod.NAME,
        version = YzljcBansMod.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.8.9]"
)
public class YzljcBansMod {
    public static final String MODID = "yzljcbans";
    public static final String NAME = "Yzljc Bans";
    public static final String VERSION = "1.8.9";

    private static final BanSequenceManager BAN_SEQUENCE_MANAGER = new BanSequenceManager();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new GetBanCommand(BAN_SEQUENCE_MANAGER));
        MinecraftForge.EVENT_BUS.register(BAN_SEQUENCE_MANAGER);
    }
}
