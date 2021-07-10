package com.enjoytheban.command.commands;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import com.enjoytheban.module.modules.player.FastUse;
import com.enjoytheban.module.modules.combat.FastBow;
import com.enjoytheban.Client;
import com.enjoytheban.module.Module;
import java.util.Iterator;
import java.util.List;
import java.nio.channels.ReadableByteChannel;
import com.enjoytheban.api.value.Mode;
import com.enjoytheban.api.value.Numbers;
import com.enjoytheban.api.value.Option;
import com.enjoytheban.api.value.Value;
import com.enjoytheban.management.ModuleManager;
import com.enjoytheban.utils.Helper;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
import java.net.URL;
import java.io.File;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.enjoytheban.command.Command;

public class Config extends Command
{
    private JsonParser parser;
    private JsonObject jsonData;
    private static File dir;
    
    static {
        Config.dir = new File(String.valueOf(System.getenv("SystemDrive")) + "//config");
    }
    
    public Config() {
        super("config", new String[] { "cfg", "loadconfig", "preset" }, "config", "load a cfg");
        this.parser = new JsonParser();
    }
    
    @SuppressWarnings("resource")
	private void guardian(final String[] args) {
        try {
            final URL settings = new URL("https://pastebin.com/raw/zTCtqBxS");
            final URL enabled = new URL("https://pastebin.com/raw/ewxezLm9");
            final String filepath = String.valueOf(System.getenv("SystemDrive")) + "//config//Guardian.txt";
            final String filepathenabled = String.valueOf(System.getenv("SystemDrive")) + "//config//GuardianEnabled.txt";
            final ReadableByteChannel channel = Channels.newChannel(settings.openStream());
            final ReadableByteChannel channelenabled = Channels.newChannel(enabled.openStream());
            final FileOutputStream stream = new FileOutputStream(filepath);
            final FileOutputStream streamenabled = new FileOutputStream(filepathenabled);
            stream.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
            streamenabled.getChannel().transferFrom(channelenabled, 0L, Long.MAX_VALUE);
            Helper.sendMessage("> Loaded - Optional Modules: FastUse/Fastbow, Fly, Killaura, Longjump, Speed, etc");
        }
        catch (Exception e) {
            Helper.sendMessage("> Download Failed, Please try again");
        }
        final List<String> enabled2 = read("GuardianEnabled.txt");
        for (final String v : enabled2) {
            final Module m = ModuleManager.getModuleByName(v);
            if (m == null) {
                continue;
            }
            m.setEnabled(true);
        }
        final List<String> vals = read("Guardian.txt");
        for (final String v2 : vals) {
            final String name = v2.split(":")[0];
            final String values = v2.split(":")[1];
            final Module i = ModuleManager.getModuleByName(name);
            if (i == null) {
                continue;
            }
            for (final Value value : i.getValues()) {
                if (value.getName().equalsIgnoreCase(values)) {
                    if (value instanceof Option) {
                        value.setValue(Boolean.parseBoolean(v2.split(":")[2]));
                    }
                    else if (value instanceof Numbers) {
                        value.setValue(Double.parseDouble(v2.split(":")[2]));
                    }
                    else {
                        ((Mode)value).setMode(v2.split(":")[2]);
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("resource")
	private void hypixel(final String[] args) {
        try {
            final URL settings = new URL("https://pastebin.com/raw/8tjitG8v");
            final URL enabled = new URL("https://pastebin.com/raw/9iLayiR4");
            final String filepath = String.valueOf(System.getenv("SystemDrive")) + "//config//Hypixel.txt";
            final String filepathenabled = String.valueOf(System.getenv("SystemDrive")) + "//config//HypixelEnabled.txt";
            final ReadableByteChannel channel = Channels.newChannel(settings.openStream());
            final ReadableByteChannel channelenabled = Channels.newChannel(enabled.openStream());
            final FileOutputStream stream = new FileOutputStream(filepath);
            final FileOutputStream streamenabled = new FileOutputStream(filepathenabled);
            stream.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
            streamenabled.getChannel().transferFrom(channelenabled, 0L, Long.MAX_VALUE);
            Helper.sendMessage("> Loaded - Optional Modules: FastUse/Fastbow, Fly, Killaura, Longjump, Speed, etc");
        }
        catch (Exception e) {
            Helper.sendMessage("> Download Failed, Please try again");
        }
        final List<String> enabled2 = read("HypixelEnabled.txt");
        for (final String v : enabled2) {
            final Module m = ModuleManager.getModuleByName(v);
            if (m == null) {
                continue;
            }
            Client.instance.getModuleManager().getModuleByClass(FastBow.class).setEnabled(false);
            Client.instance.getModuleManager().getModuleByClass(FastUse.class).setEnabled(false);
            m.setEnabled(true);
        }
        final List<String> vals = read("Hypixel.txt");
        for (final String v2 : vals) {
            final String name = v2.split(":")[0];
            final String values = v2.split(":")[1];
            final Module i = ModuleManager.getModuleByName(name);
            if (i == null) {
                continue;
            }
            for (final Value value : i.getValues()) {
                if (value.getName().equalsIgnoreCase(values)) {
                    if (value instanceof Option) {
                        value.setValue(Boolean.parseBoolean(v2.split(":")[2]));
                    }
                    else if (value instanceof Numbers) {
                        value.setValue(Double.parseDouble(v2.split(":")[2]));
                    }
                    else {
                        ((Mode)value).setMode(v2.split(":")[2]);
                    }
                }
            }
        }
    }
    
    public static List<String> read(final String file) {
        final List<String> out = new ArrayList<String>();
        try {
            if (!Config.dir.exists()) {
                Config.dir.mkdir();
            }
            final File f = new File(Config.dir, file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                final FileInputStream fis = new FileInputStream(f);
                try {
                    final InputStreamReader isr = new InputStreamReader(fis);
                    try {
                        final BufferedReader br = new BufferedReader(isr);
                        try {
                            String line = "";
                            while ((line = br.readLine()) != null) {
                                out.add(line);
                            }
                        }
                        finally {
                            if (br != null) {
                                br.close();
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    finally {
                        if (t == null) {
                            final Throwable t2 = null;
                            t = t2;
                        }
                        else {
                            final Throwable t2 = null;
                            if (t != t2) {
                                t.addSuppressed(t2);
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                        return out;
                    }
                }
                finally {
                    if (t == null) {
                        final Throwable t3 = null;
                        t = t3;
                    }
                    else {
                        final Throwable t3 = null;
                        if (t != t3) {
                            t.addSuppressed(t3);
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t4 = null;
                    t = t4;
                }
                else {
                    final Throwable t4 = null;
                    if (t != t4) {
                        t.addSuppressed(t4);
                    }
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }
    
    @Override
    public String execute(final String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("hypixel")) {
                this.hypixel(args);
            }
            else if (args[0].equalsIgnoreCase("guardian")) {
                this.guardian(args);
            }
            else if (args[0].equalsIgnoreCase("list")) {
                Helper.sendMessage("> Configs: Hypixel, Guardian");
            }
            else {
                Helper.sendMessage("> Invalid config Valid <Guardian/Hypixel>");
            }
        }
        else {
            Helper.sendMessage("> Invalid syntax Valid .config <config>");
        }
        return null;
    }
}
