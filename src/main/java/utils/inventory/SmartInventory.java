package utils.inventory;

import me.zkingofkill.factionscore.Main;
import utils.inventory.content.InventoryContents;
import utils.inventory.content.InventoryProvider;
import utils.inventory.opener.InventoryOpener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class SmartInventory {

    /*
    https://minuskube.gitbooks.io/smartinvs/
     */

    private String id;
    private String title;
    private InventoryType type;
    private int rows, columns;
    private boolean closeable;
    private boolean skipOtherClickLocations = true;
    private boolean editable = false;

    private InventoryProvider provider;
    private SmartInventory parent;

    private List<InventoryListener<? extends Event>> listeners;
    private InventoryManager manager;

    private SmartInventory(InventoryManager manager) {
        this.manager = manager;
    }

    public Inventory open(Player player) {
        return open(player, 0);
    }

    public Inventory open(Player player, int page) {
        Optional<SmartInventory> oldInv = this.manager.getInventory(player);

        oldInv.ifPresent(inv -> {
            inv.getListeners().stream()
                    .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                    .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener)
                            .accept(new InventoryCloseEvent(player.getOpenInventory())));

            this.manager.setInventory(player, null);
        });

        InventoryContents contents = new InventoryContents.Impl(this, player);
        contents.pagination().page(page);

        this.manager.setContents(player, contents);
        this.provider.init(player, contents);

        InventoryOpener opener = this.manager.findOpener(type)
                .orElseThrow(() -> new IllegalStateException("No opener found for the inventory type " + type.name()));
        Inventory handle = opener.open(this, player);

        this.manager.setInventory(player, this);

        return handle;
    }

    @SuppressWarnings("unchecked")
    public void close(Player player) {
        listeners.stream()
                .filter(listener -> listener.getType() == InventoryCloseEvent.class)
                .forEach(listener -> ((InventoryListener<InventoryCloseEvent>) listener)
                        .accept(new InventoryCloseEvent(player.getOpenInventory())));

        this.manager.setInventory(player, null);
        player.closeInventory();

        this.manager.setContents(player, null);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public InventoryType getType() {
        return type;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isCloseable() {
        return closeable;
    }

    public void setCloseable(boolean closeable) {
        this.closeable = closeable;
    }

    public InventoryProvider getProvider() {
        return provider;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isSkipOtherClickLocations() {
        return skipOtherClickLocations;
    }

    public void setSkipOtherClickLocations(boolean skipOtherClickLocations) {
        this.skipOtherClickLocations = skipOtherClickLocations;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Optional<SmartInventory> getParent() {
        return Optional.ofNullable(parent);
    }

    public InventoryManager getManager() {
        return manager;
    }

    List<InventoryListener<? extends Event>> getListeners() {
        return listeners;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String id = "unknown";
        private String title = "";
        private InventoryType type = InventoryType.CHEST;
        private int rows = 6, columns = 9;
        private boolean closeable = true;
        private boolean editable = false;
        private boolean skipOtherClickLocations = false;
        private InventoryManager manager;
        private InventoryProvider provider;
        private SmartInventory parent;

        private List<InventoryListener<? extends Event>> listeners = new ArrayList<>();

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }


        public Builder setSkipOtherClickLocations(boolean skipOtherClickLocations) {
            this.skipOtherClickLocations = skipOtherClickLocations;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder type(InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder size(int rows, int columns) {
            this.rows = rows;
            this.columns = columns;
            return this;
        }

        public Builder closeable(boolean closeable) {
            this.closeable = closeable;
            return this;
        }

        public Builder editable(boolean editable) {
            this.editable = editable;
            return this;
        }

        public Builder provider(InventoryProvider provider) {
            this.provider = provider;
            return this;
        }

        public Builder parent(SmartInventory parent) {
            this.parent = parent;
            return this;
        }

        public Builder listener(InventoryListener<? extends Event> listener) {
            this.listeners.add(listener);
            return this;
        }

        public Builder manager(InventoryManager manager) {
            this.manager = manager;
            return this;
        }

        public SmartInventory build() {
            if (this.provider == null)
                throw new IllegalStateException("The provider of the SmartInventory.Builder must be set.");

            InventoryManager manager = this.manager != null ? this.manager : Main.singleton.inventoryManager;

            SmartInventory inv = new SmartInventory(manager);
            inv.id = this.id;
            inv.title = this.title;
            inv.type = this.type;
            inv.rows = this.rows;
            inv.columns = this.columns;
            inv.closeable = this.closeable;
            inv.provider = this.provider;
            inv.parent = this.parent;
            inv.listeners = this.listeners;
            inv.editable = this.editable;
            inv.skipOtherClickLocations = this.skipOtherClickLocations;

            return inv;
        }
    }

}