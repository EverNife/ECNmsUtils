package br.com.finalcraft.ecnmsutils.v1_20_R2;

import br.com.finalcraft.ecnmsutils.NmsRevision;
import br.com.finalcraft.ecnmsutils.anvil.AbstractNmsAnvil;
import br.com.finalcraft.ecnmsutils.anvil.AnvilHandle;
import br.com.finalcraft.ecnmsutils.anvil.AnvilRequest;
import br.com.finalcraft.ecnmsutils.v1_20_R1.PromptMenuV1_20;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.InventoryClickType;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_20_R2.util.CraftChatMessage;
import org.bukkit.entity.Player;

final class AnvilV1_20_R2 extends AbstractNmsAnvil {

    AnvilV1_20_R2() {
        super(NmsRevision.V1_20_R2);
    }

    @Override
    protected AnvilHandle openPrompt(Player player, AnvilRequest request) {
        return new PromptContainer(player, request).menu.open(player);
    }

    static final class PromptContainer extends ContainerAnvil {

        private final PromptMenuV1_20 menu;

        private PromptContainer(Player player, AnvilRequest request) {
            super(((CraftPlayer) player).getHandle().nextContainerCounter(), ((CraftInventoryPlayer) player.getInventory()).getInventory());
            checkReachable = false; //no anvil block backs this window, so there is nothing to stand near
            menu = new PromptMenuV1_20(this, player, request, CraftChatMessage.fromString(PromptMenuV1_20.titleText(request))[0]);
        }

        @Override
        public boolean a(String text) {
            menu.rename(text);
            return true;
        }

        @Override
        public void m() {
            //vanilla's recompute would write a repair cost and a repaired item; the result slot is the session's
        }

        @Override
        public void a(int slot, int button, InventoryClickType type, EntityHuman human) {
            menu.click(slot);
        }

        @Override
        public void b(EntityHuman human) {
            menu.beforeClose();
            super.b(human);
            menu.afterClose();
        }

    }

}
