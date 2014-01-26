UnitTest {
  var <assertionCount = 0, <successCount = 0, <failureCount = 0, <envir = nil;

  *new {
    ^super.new.init;
  }

  init {
    envir = (unitTest: this);
  }

  // Main testing environment
  test {
    arg name, func;

    // Run the setup procedure for each test
    envir[\setupProcedure].notNil.if {envir = envir[\setupProcedure].value(envir);};

    ("Testing: " ++ name).postln;
    func.inEnvir(envir).value(envir);

    // Run the takedown procedure after each test
    envir[\takedownProcedure].notNil.if {envir = envir[\takedownProcedure].value(envir);};

    ("Assertions: " ++ assertionCount ++ "\t Successes: " ++ successCount ++ "\t Failures: " ++ failureCount).postln;
  }

  // Each setup block must accept the environment as input,
  // and return the environment variable after adding to it.
  setup {
    arg setupProcedure;
    envir[\setupProcedure] = setupProcedure;
    ^envir;
  }

  // Each setup block must accept the environment as input,
  // remove all procedures that setup has done, and return the envir.
  takedown {
    arg takedownProcedure;
    envir[\takedownProcedure] = takedownProcedure;
    ^envir;
  }

  // Must evaluate to a Boolean to be valid
  assert {
    arg assertion;
    ^this.judgeTruthValue(assertion.value);
  }

  // [+assertion+] is the function that is evaulated
  // [+expected+] is a constant value
  assertEqual {
    arg assertion, expected;
    // [assertion, expected].postln;
    ^this.judgeTruthValue(assertion.value == expected);
  }

  assertInDelta {
    arg assertion, expected, delta;
    ^this.judgeTruthValue((assertion.value - expected).abs <= delta);
  }

  // Methods for convenience
  judgeTruthValue {
    arg truthValue;
    assertionCount = assertionCount + 1;
    if(truthValue,
      { successCount = successCount + 1 },
      { failureCount = failureCount + 1 }
    );
    ^truthValue;
  }
}

// Added functionality to let us use various methods within calls
+ Function {
  assert {
    var u = currentEnvironment[\unitTest];
    u.notNil.if {u.assert(this)};
  }

  assertEqual {
    arg expected;
    var u = currentEnvironment[\unitTest];
    u.notNil.if {u.assertEqual(this, expected)};
  }

  assertInDelta {
    arg expected, delta;
    var u = currentEnvironment[\unitTest];
    u.notNil.if {u.assertInDelta(this, expected, delta)};
  }
}
