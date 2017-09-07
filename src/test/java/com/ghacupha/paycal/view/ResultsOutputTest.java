package com.ghacupha.paycal.view;

import com.ghacupha.paycal.models.ResultsOutput;
import com.ghacupha.paycal.utils.TestUtils;

/**
 * Created by edwin.njeru on 29/08/2017.
 */
public class ResultsOutputTest extends TestUtils<ResultsOutput> {

    /**
     * This method returns an instance of the bean being tested
     *
     * @return
     */
    @Override
    public ResultsOutput getBeanInstance() {

        return new ResultsOutput(new DisplayImpl());
    }
}