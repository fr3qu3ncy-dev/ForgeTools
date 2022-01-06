package de.fr3qu3ncy.forgetools.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

public class SaveDataHandler implements INBTSerializable<CompoundTag> {

    private final Map<String, Object> data;

    public SaveDataHandler() {
        this.data = new HashMap<>();
    }

    public void putData(String key, Object obj) {
        this.data.put(key, obj);
    }

    public boolean getBool(String key, boolean def) {
        if (!data.containsKey(key)) return def;
        return (boolean) data.get(key);
    }

    public String getString(String key, String def) {
        if (!data.containsKey(key)) return def;
        return (String) data.get(key);
    }

    public int getInt(String key, int def) {
        if (!data.containsKey(key)) return def;
        return (int) data.get(key);
    }

    public Set<Map.Entry<String, Object>> getAllData() {
        return this.data.entrySet();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        getAllData().forEach(entry -> {
            nbt.putString(entry.getKey(), entry.getValue().toString());
        });
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nbt.getAllKeys().forEach(key -> {
            putData(key, nbt.getBoolean(key));
            System.out.println("Deserialized " + key + " " + nbt.getBoolean(key));
        });
    }

    public static class Factory implements Callable<SaveDataHandler> {

        @Override
        public SaveDataHandler call() {
            return new SaveDataHandler();
        }
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        private final Capability<SaveDataHandler> capability;
        private final LazyOptional<SaveDataHandler> implementation;

        public Provider(Capability<SaveDataHandler> capability, LazyOptional<SaveDataHandler> implementation) {
            this.capability = capability;
            this.implementation = implementation;
        }

        public static Provider from(Capability<SaveDataHandler> cap, NonNullSupplier<SaveDataHandler> impl) {
            return new Provider(cap, LazyOptional.of(impl));
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return implementation.cast();
        }

        @Override
        public CompoundTag serializeNBT() {
            AtomicReference<CompoundTag> tag = new AtomicReference<>(new CompoundTag());
            implementation.resolve().ifPresent(itemHandler -> {
                tag.set(itemHandler.serializeNBT());
            });
            return tag.get();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            implementation.resolve().ifPresent(itemHandler -> {
                itemHandler.deserializeNBT(nbt);
            });
        }

        public void invalidate() {
            implementation.invalidate();
        }
    }
}