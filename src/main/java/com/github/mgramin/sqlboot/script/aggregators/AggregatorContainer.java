package com.github.mgramin.sqlboot.script.aggregators;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mgramin on 24.12.2016.
 */
public class AggregatorContainer {

    private List<IAggregator> aggregators = new ArrayList<>();

    public List<IAggregator> getAggregators() {
        return aggregators;
    }

    public void setAggregators(List<IAggregator> aggregators) {
        this.aggregators = aggregators;
    }
}
