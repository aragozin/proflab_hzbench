package info.ragozin.proflab.hzbench.load;

import org.junit.Test;

import info.ragozin.proflab.hzbench.demo.HzService;

public class HzLoadRunnerCheck {

    @Test
    public void runLoad() {
        HzLoadRunner runner = new HzLoadRunner(HzService.client());
        runner.run();
    }
}
