package io.ticktok.server.clock.repository;

import com.google.common.collect.ImmutableMap;
import io.ticktok.server.clock.Clock;
import io.ticktok.server.clock.ClocksFinder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.ticktok.server.clock.repository.ClocksRepository.not;
import static java.util.stream.Collectors.toMap;

public class RepositoryClocksFinder implements ClocksFinder {

    private final ClocksRepository repository;

    public RepositoryClocksFinder(ClocksRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Clock> findBy(Map<String, String> params) {
        return repository.findBy(filterParameterWithPendingExcluded(params));
    }

    private Map<String, String> filterParameterWithPendingExcluded(Map<String, String> params) {
        return Stream.of(params, ImmutableMap.of("status", not(Clock.PENDING)))
                    .flatMap(map -> map.entrySet().stream())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (left, right) -> left));
    }

    public Clock findById(String id) {
        return repository.findById(id).orElseThrow(
                () -> new ClockNotFoundException("Failed to find clock with id: " + id));
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Clock not found")
    public static class ClockNotFoundException extends RuntimeException {

        public ClockNotFoundException(String message) {
            super(message);
        }
    }
}
