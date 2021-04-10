package com.npdevs.riseup.captureservice.predictivemodels;

public interface Classifier {
    String name();

    Classification recognize(final float[] pixels);
}
