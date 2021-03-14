package net.minecraft.client.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.util.JsonUtils;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

public class ResourceIndex
{
    private static final Logger logger = LogManager.getLogger();
    private final Map resourceMap = Maps.newHashMap();

    public ResourceIndex(File p_i1047_1_, String p_i1047_2_)
    {
        if (p_i1047_2_ != null)
        {
            File var3 = new File(p_i1047_1_, "objects");
            File var4 = new File(p_i1047_1_, "indexes/" + p_i1047_2_ + ".json");
            BufferedReader var5 = null;

            try
            {
                var5 = Files.newReader(var4, Charsets.UTF_8);
                JsonObject var6 = (new JsonParser()).parse(var5).getAsJsonObject();
                JsonObject var7 = JsonUtils.getJsonObjectFieldOrDefault(var6, "objects", (JsonObject)null);

                if (var7 != null)
                {
                    Iterator var8 = var7.entrySet().iterator();

                    while (var8.hasNext())
                    {
                        Entry var9 = (Entry)var8.next();
                        JsonObject var10 = (JsonObject)var9.getValue();
                        String var11 = (String)var9.getKey();
                        String[] var12 = var11.split("/", 2);
                        String var13 = var12.length == 1 ? var12[0] : var12[0] + ":" + var12[1];
                        String var14 = JsonUtils.getJsonObjectStringFieldValue(var10, "hash");
                        File var15 = new File(var3, var14.substring(0, 2) + "/" + var14);
                        this.resourceMap.put(var13, var15);
                    }
                }
            }
            catch (JsonParseException var20)
            {
                logger.error("Unable to parse resource index file: " + var4);
            }
            catch (FileNotFoundException var21)
            {
                logger.error("Can\'t find the resource index file: " + var4);
            }
            finally
            {
                IOUtils.closeQuietly(var5);
            }
        }
    }

    public Map getResourceMap()
    {
        return this.resourceMap;
    }
}
