package com.trendyol.model;

public class Result {

    public static class ResultGroupProduct {
        public String productId;
        public Long cnt;

        public ResultGroupProduct() {
            super();
        }

        public ResultGroupProduct(String productId, Long cnt) {
            this.productId = productId;
            this.cnt = cnt;
        }

        @Override
        public String toString() {
            return  productId + "|" + cnt;
        }
    }

    public static class ResultGroupEvent {
        public String eventName;
        public Long cnt;

        public ResultGroupEvent() {
            super();
        }

        public ResultGroupEvent(String eventName, Long cnt) {
            this.eventName = eventName;
            this.cnt = cnt;
        }

        @Override
        public String toString() {
            return  eventName + "|" + cnt;
        }
    }
    public static class ResultFullfilledEvents {
        public String userId;
        public Long cnt;

        public ResultFullfilledEvents() {
            super();
        }

        public ResultFullfilledEvents(String userId,Long cnt) {
            this.userId = userId;
            this.cnt = cnt;

        }

        @Override
        public String toString() {
            return userId +"|" + cnt;
        }
    }

    public static class ResultFullfilledProductId {
        public String productId;

        public ResultFullfilledProductId() {
            super();
        }

        public ResultFullfilledProductId(String productId) {
            this.productId = productId;

        }

        @Override
        public String toString() {
            return  productId;
        }
    }

}