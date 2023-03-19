package com.easyapp.pix;

public final class Payload {

    private final Type type;
    private final String formatIndicator;
    private final String pointMethod;
    private final String txid;
    private final String url;
    private final String currency;
    private final String amount;
    private final String key;
    private final String name;
    private final String city;
    private final String country;
    private final String category;

    private Payload(Builder builder) {
        this.type = builder.type;
        this.formatIndicator = builder.formatIndicator;
        this.pointMethod = builder.pointMethod;
        this.txid = builder.txid;
        this.url = builder.url;
        this.currency = builder.currency;
        this.amount = builder.amount;
        this.key = builder.key;
        this.name = builder.name;
        this.city = builder.city;
        this.country = builder.country;
        this.category = builder.category;
    }

    public static Builder builder(Type type) {
        return new Builder(type);
    }

    public Type getType() {
        return type;
    }

    public String getFormatIndicator() {
        return formatIndicator;
    }

    public String getPointMethod() {
        return pointMethod;
    }

    public String getTxid() {
        return txid;
    }

    public String getUrl() {
        return url;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAmount() {
        return amount;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getCategory() {
        return category;
    }

    public boolean hasFormatIndicator() {
        return formatIndicator != null && !formatIndicator.isEmpty();
    }

    public boolean hasPointMethod() {
        return pointMethod != null && !pointMethod.isEmpty();
    }

    public boolean hasTxid() {
        return txid != null && !txid.isEmpty();
    }

    public boolean hasUrl() {
        return url != null && !url.isEmpty();
    }

    public boolean hasCurrency() {
        return currency != null && !currency.isEmpty();
    }

    public boolean hasAmount() {
        return amount != null && !amount.isEmpty();
    }

    public boolean hasKey() {
        return key != null && !key.isEmpty();
    }

    public boolean hasName() {
        return name != null && !name.isEmpty();
    }

    public boolean hasCity() {
        return city != null && !city.isEmpty();
    }

    public boolean hasCountry() {
        return country != null && !country.isEmpty();
    }

    public boolean hasCategory() {
        return category != null && !category.isEmpty();
    }

    public boolean isDynamic() {
        return type == Type.DYNAMIC;
    }

    public boolean isStatic() {
        return type == Type.STATIC;
    }

    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (PixException e) {
            return false;
        }
    }

    public void validate() throws PixException {
        if (!hasCurrency()) {
            throw new PixException("Payload currency is require.");
        }
        if (!hasCountry()) {
            throw new PixException("Payload country is require.");
        }
        if (category.length() < 4) {
            throw new PixException("Payload category must have length of 4.");
        }
        if (!hasName()) {
            throw new PixException("Payload name is require.");
        }
        if (!hasCity()) {
            throw new PixException("Payload city is require.");
        }
        if (type == Payload.Type.DYNAMIC) {
            if (!hasPointMethod()) {
                throw new PixException("Payload pointMethod is require in dynamic.");
            }
            if (!hasUrl()) {
                throw new PixException("Payload pointMethod is require in dynamic.");
            }
        }
    }

    public enum Type {
        STATIC, DYNAMIC
        }

    public static class Builder {
        private Type type;
        private String formatIndicator;
        private String pointMethod;
        private String txid;
        private String url;
        private String description;
        private String currency;
        private String amount;
        private String key;
        private String name;
        private String city;
        private String country;
        private String category;

        {
            this.type = Type.STATIC;
            this.formatIndicator = "01";
            this.txid = "***";
            this.category = "0000";
        }

        public Builder(Type type) {
            if (type != null) {
                this.type = type;
            }
        }

        public Builder formatIndicator(String format) {
            if (format != null && !format.isEmpty()) {
                this.formatIndicator = format;
            }
            return this;
        }

        public Builder pointMethod(String pointMethod) {
            this.pointMethod = pointMethod;
            return this;
        }

        public Builder txId(String txid) {
            if (txid != null && !txid.isEmpty()) {
                this.txid = txid;
            }
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder amount(String amount) {
            this.amount = amount;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder category(String category) {
            if (category != null && !category.isEmpty()) {
                this.category = category;
            }
            return this;
        }

        public Payload build() {
            return new Payload(this);
        }
    }


}
