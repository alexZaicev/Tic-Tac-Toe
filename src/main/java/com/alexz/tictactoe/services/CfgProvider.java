package com.alexz.tictactoe.services;

import com.alexz.tictactoe.models.ConfigKey;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CfgProvider extends ServiceBase {

  private static final Logger _logger = LogManager.getLogger(CfgProvider.class);
  private static final String CFG_FILE = "application.properties";
  private static final Map<String, Object> config = new HashMap<>();
  private static final Object lock = new Object();
  private static CfgProvider instance;

  public static CfgProvider getInstance() {
    CfgProvider result = instance;
    if (result == null) {
      synchronized (lock) {
        result = instance;
        if (result == null) instance = result = new CfgProvider();
      }
    }
    return result;
  }

  @Override
  public void init() {
    final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(CFG_FILE);
    if (stream == null) {
      _logger.error("Failed to load configuration from file");
      System.exit(1);
    }
    try {
      final Properties props = new Properties();
      props.load(stream);
      for (final Object key : props.keySet()) {
        Object value = props.get(key);

        final String sKey = (String) key;
        if (sKey.startsWith("font")) {
          value = this.parseFont((String) value);
          if (value == null) {
            _logger.error("Invalid font format [" + sKey + "]");
            continue;
          }
        }
        config.put(sKey, value);
      }
    } catch (final IOException ex) {
      _logger.error("Failed to load configuration from file", ex);
      System.exit(1);
    }
  }

  public Object get(final ConfigKey key) {
    if (key != null && config.containsKey(key.name)) {
      return config.get(key.name);
    }
    throw new IllegalArgumentException("Configuration does not exist for key [" + key + "]");
  }

  public String getStr(final ConfigKey key) {
    return (String) this.get(key);
  }

  public int getInt(final ConfigKey key) {
    return Integer.parseInt(this.getStr(key));
  }

  public long getLong(final ConfigKey key) {
    return Long.parseLong(this.getStr(key));
  }

  public double getDouble(final ConfigKey key) {
    return Double.parseDouble(this.getStr(key));
  }

  private Font parseFont(final String val) {
    Font font = null;
    if (StringUtils.isNotBlank(val)) {
      final String[] opts = val.split(",");

      if (opts.length == 3) {

        if (NumberUtils.isParsable(opts[1]) && NumberUtils.isParsable(opts[2])) {
          int weight = Integer.parseInt(opts[1]);
          if (weight < 0 || weight > 2) {
            weight = 0;
          }
          final int size = Integer.parseInt(opts[2]);
          font = new Font(opts[0], weight, size);
        }
      }
    }
    return font;
  }
}
