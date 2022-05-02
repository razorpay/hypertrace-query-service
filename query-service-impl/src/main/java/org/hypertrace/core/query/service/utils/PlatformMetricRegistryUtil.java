package org.hypertrace.core.query.service.utils;

import com.google.common.base.Preconditions;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Tag;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hypertrace.core.serviceframework.metrics.PlatformMetricsRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// this should be added in utils repo or change should be made in platform metrics file of service
// framework repo
public class PlatformMetricRegistryUtil extends PlatformMetricsRegistry {

  private static final Logger LOG = LoggerFactory.getLogger(PlatformMetricRegistryUtil.class);

  public static DistributionSummary registerDistributionSummary(
      String name, Map<String, String> tags, boolean histogram) {
    try {
      Set<Tag> newTags = new HashSet<>();
      Preconditions.checkNotNull(tags).forEach((k, v) -> newTags.add(new ImmutableTag(k, v)));
      io.micrometer.core.instrument.DistributionSummary.Builder builder =
          DistributionSummary.builder(name).tags(newTags).maximumExpectedValue(604800.0); //minutes for 7 days
      if (histogram) {
        builder.publishPercentileHistogram();
      }
      return builder.register(getMeterRegistry());
    } catch (Exception e) {
      LOG.error("Error occurred while registering {} meter ", name, e);
      return null;
    }
  }
}
