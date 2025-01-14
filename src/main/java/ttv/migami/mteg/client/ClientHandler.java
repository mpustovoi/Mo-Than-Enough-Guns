package ttv.migami.mteg.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ttv.migami.jeg.client.CustomGunManager;
import ttv.migami.jeg.client.render.gun.ModelOverrides;
import ttv.migami.jeg.enchantment.EnchantmentTypes;
import ttv.migami.jeg.item.GunItem;
import ttv.migami.mteg.Reference;
import ttv.migami.mteg.client.render.gun.model.*;
import ttv.migami.mteg.client.screen.MoWorkbenchScreen;
import ttv.migami.mteg.init.ModBlocks;
import ttv.migami.mteg.init.ModContainers;
import ttv.migami.mteg.init.ModItems;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ClientHandler {

    public static void setup() {
        setupRenderLayers();
        registerModelOverrides();
        registerScreenFactories();
    }

    private static void setupRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.MO_WORKBENCH.get(), RenderType.cutout());
    }

    private static void registerModelOverrides() {
        ModelOverrides.register(ModItems.SCAR_L.get(), new ScarLModel());
        ModelOverrides.register(ModItems.G36C.get(), new G36CModel());
        ModelOverrides.register(ModItems.AKM.get(), new AKMModel());
        ModelOverrides.register(ModItems.AS_VAL.get(), new ASVALModel());
        ModelOverrides.register(ModItems.FAMAS.get(), new FamasModel());
        ModelOverrides.register(ModItems.M1_GARAND.get(), new M1GarandModel());
        ModelOverrides.register(ModItems.THOMPSON.get(), new ThompsonModel());
        ModelOverrides.register(ModItems.AKM_CUSTOM.get(), new AKMCustomModel());
        ModelOverrides.register(ModItems.AWP.get(), new AWPModel());
        ModelOverrides.register(ModItems.BENELLI_M4.get(), new BenelliModel());
        ModelOverrides.register(ModItems.GLOCK_17.get(), new Glock17Model());
        ModelOverrides.register(ModItems.M14_EBR.get(), new M14EBRModel());
        ModelOverrides.register(ModItems.M14.get(), new M14Model());
        ModelOverrides.register(ModItems.M1911.get(), new M1911Model());
        ModelOverrides.register(ModItems.MOSSBERG_590.get(), new MossbergModel());
        ModelOverrides.register(ModItems.REMINGTON_870.get(), new Remington870Model());
        ModelOverrides.register(ModItems.SCAR_H.get(), new ScarHModel());
        ModelOverrides.register(ModItems.VSS_VINTOREZ.get(), new VSSVintorezModel());
        ModelOverrides.register(ModItems.WALTHER_PPK.get(), new WaltherPPKModel());
        ModelOverrides.register(ModItems.WELROD.get(), new WelrodModel());
        ModelOverrides.register(ModItems.LANCHESTER.get(), new LanchesterModel());
        ModelOverrides.register(ModItems.PPSH.get(), new PPSH41Model());
        ModelOverrides.register(ModItems.WRAPPED_RIFLE.get(), new WrappedRifleModel());
        ModelOverrides.register(ModItems.HELLFIRE.get(), new HellfireModel());
        ModelOverrides.register(ModItems.BLUE_HEAT.get(), new BlueHeatModel());
        ModelOverrides.register(ModItems.HOG_BONKER.get(), new HogBonkerModel());
        ModelOverrides.register(ModItems.HOG_BONKER.get(), new HogBonkerModel());
        ModelOverrides.register(ModItems.DOUBLE_BARREL.get(), new DoubleBarrelModel());

        // Non animated models
        ModelOverrides.register(ModItems.MP5.get(), new MP5Model());
        ModelOverrides.register(ModItems.FLARE_GUN.get(), new FlareGunModel());
        ModelOverrides.register(ModItems.BAKER.get(), new BakerRifleModel());
        ModelOverrides.register(ModItems.UZI.get(), new UziModel());
        ModelOverrides.register(ModItems.MICRO_UZI.get(), new MicroUziModel());
        ModelOverrides.register(ModItems.M16A1.get(), new M16A1Model());
        ModelOverrides.register(ModItems.M2_FLAMETHROWER.get(), new M2FlamethrowerModel());
        ModelOverrides.register(ModItems.FLAMER.get(), new FlamerModel());
    }

    private static void registerScreenFactories() {
        MenuScreens.register(ModContainers.MO_WORKBENCH.get(), MoWorkbenchScreen::new);
    }

    public static void onRegisterCreativeTab(IEventBus bus)
    {
        DeferredRegister<CreativeModeTab> register = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MOD_ID);
        CreativeModeTab.Builder builder = CreativeModeTab.builder();
        builder.title(Component.translatable("itemGroup." + Reference.MOD_ID));
        builder.icon(() -> {
            ItemStack stack = new ItemStack(ModItems.SCAR_L.get());
            stack.getOrCreateTag().putBoolean("IgnoreAmmo", true);
            return stack;
        });
        builder.displayItems((flags, output) ->
        {
            ModItems.ITEMS.getEntries().forEach(registryObject ->
            {
                if(registryObject.get() instanceof GunItem item)
                {
                    ItemStack stack = new ItemStack(item);
                    stack.getOrCreateTag().putInt("AmmoCount", item.getGun().getReloads().getMaxAmmo());
                    output.accept(stack);
                    return;
                }
                output.accept(registryObject.get());
            });
            CustomGunManager.fill(output);
            for(Enchantment enchantment : ForgeRegistries.ENCHANTMENTS)
            {
                if(enchantment.category == EnchantmentTypes.GUN || enchantment.category == EnchantmentTypes.SEMI_AUTO_GUN)
                {
                    output.accept(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, enchantment.getMaxLevel())), CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
                }
            }
        });
        register.register("creative_tab", builder::build);
        register.register(bus);
    }

}