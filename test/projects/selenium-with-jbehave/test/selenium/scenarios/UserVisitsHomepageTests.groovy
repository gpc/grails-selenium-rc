package scenarios

import org.jbehave.scenario.Scenario
import org.jbehave.scenario.steps.CandidateSteps

class UserVisitsHomepageTests extends Scenario {
  public UserVisitsHomepageTests() {
        super([new HomepageSteps()] as CandidateSteps[])
    }
}
