package com.safezone;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * Fails the build the moment a module reaches into another module's internals
 * instead of going through its public API or an event - this is the guardrail
 * requested up front, wired in from day one rather than retrofitted later.
 */
class ModularityTests {

    static final ApplicationModules MODULES = ApplicationModules.of(SafezoneBackendApplication.class);

    @Test
    void verifiesModuleBoundaries() {
        assertThatCode(MODULES::verify).doesNotThrowAnyException();
    }

    @Test
    void writesModuleDocumentation() {
        new Documenter(MODULES).writeDocumentation();
    }
}
