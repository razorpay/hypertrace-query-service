package org.hypertrace.core.query.service.htqueries.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Base64;
import org.testcontainers.shaded.org.apache.commons.lang.math.RandomUtils;

public class ByteBufferTypeAdapter
    implements JsonDeserializer<ByteBuffer>, JsonSerializer<ByteBuffer> {

  @Override
  public ByteBuffer deserialize(
      JsonElement jsonElement, Type type, JsonDeserializationContext context) {
    return ByteBuffer.wrap(
        String.valueOf(RandomUtils.nextInt(10)).getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public JsonElement serialize(ByteBuffer src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(Base64.encodeBase64String(src.array()));
  }
}
