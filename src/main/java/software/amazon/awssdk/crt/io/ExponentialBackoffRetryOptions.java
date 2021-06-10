/**
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0.
 */

package software.amazon.awssdk.crt.io;

import software.amazon.awssdk.crt.CrtResource;

public class ExponentialBackoffRetryOptions extends CrtResource {

    public enum ExponentialBackoffJitterMode {
        Default(0),

        None(1),

        Full(2),

        Decorrelated(3);

        private int jitterMode;

        ExponentialBackoffJitterMode(int jitterMode) {
            this.jitterMode = jitterMode;
        }

        int getValue() {
            return jitterMode;
        }
    }

    /**
     * Determines whether a resource releases its dependencies at the same time the native handle is released or if it waits.
     * Resources that wait are responsible for calling releaseReferences() manually.
     */
    @Override
    protected boolean canReleaseReferencesImmediately() { return true; }

    /**
     * Frees the native resources associated with this instance
     */
    @Override
    protected void releaseNativeHandle() {
        // It is perfectly acceptable for this to have never created a native resource
        if (!isNull()) {
            exponentialBackoffRetryOptionsDestroy(getNativeHandle());
        }
    }

    private ExponentialBackoffRetryOptions(ExponentialBackoffRetryOptionsBuilder builder)
    {
        super();

        acquireNativeHandle(exponentialBackoffRetryOptionsNew((builder.eventLoopGroup != null) ? builder.eventLoopGroup.getNativeHandle() : 0l,
                builder.maxRetries,
                builder.backoffScaleFactorMS,
                builder.jitterMode.getValue()));

        if(builder.eventLoopGroup != null) {
            addReferenceTo(builder.eventLoopGroup);
        }
    }

    static public class ExponentialBackoffRetryOptionsBuilder
    {
        private EventLoopGroup eventLoopGroup;
        private long maxRetries;
        private long backoffScaleFactorMS;
        private ExponentialBackoffJitterMode jitterMode;

        public ExponentialBackoffRetryOptionsBuilder() {
            jitterMode = ExponentialBackoffJitterMode.Default;
        }

        public ExponentialBackoffRetryOptionsBuilder withEventLoopGroup(EventLoopGroup eventLoopGroup) {
            this.eventLoopGroup = eventLoopGroup;
            return this;
        }

        public EventLoopGroup getEventLoopGroup() {
            return this.eventLoopGroup;
        }

        public ExponentialBackoffRetryOptionsBuilder withMaxRetries(long maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public long getMaxRetries() {
            return this.maxRetries;
        }

        public ExponentialBackoffRetryOptionsBuilder withBackoffScaleFactorMS(long backoffScaleFactorMS) {
            this.backoffScaleFactorMS = backoffScaleFactorMS;
            return this;
        }

        public long getBackoffScaleFactorMS() {
            return this.backoffScaleFactorMS;
        }

        public ExponentialBackoffRetryOptionsBuilder withJitterMode(ExponentialBackoffJitterMode jitterMode) {
            this.jitterMode = jitterMode;
            return this;
        }

        public ExponentialBackoffJitterMode getJitterMode() {
            return this.jitterMode;
        }

        public ExponentialBackoffRetryOptions build() {
            return new ExponentialBackoffRetryOptions(this);
        }
    }

    /*******************************************************************************
     * native methods
     ******************************************************************************/
    private static native long exponentialBackoffRetryOptionsNew(long elg, long maxRetries, long backoffScaleFactorMS, int jitterMode);

    private static native void exponentialBackoffRetryOptionsDestroy(long retryOptions);
}