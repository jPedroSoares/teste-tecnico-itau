CREATE TABLE IF NOT EXISTS insurance_policy (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    product_id UUID NOT NULL,
    category VARCHAR(20) NOT NULL,
    status VARCHAR(20),
    coverages JSONB,
    assistances JSONB,
    total_monthly_premium_amount NUMERIC(15, 2),
    insured_amount NUMERIC(15, 2),
    payment_method VARCHAR(20) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP,
    sales_channel VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS policy_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    insurance_policy_id UUID NOT NULL REFERENCES insurance_policy(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_insurance_policy_customer ON insurance_policy(customer_id);
CREATE INDEX IF NOT EXISTS idx_policy_history ON policy_history(insurance_policy_id);

CREATE INDEX IF NOT EXISTS idx_coverages_gin ON insurance_policy USING GIN (coverages);
CREATE INDEX IF NOT EXISTS idx_assistances_gin ON insurance_policy USING GIN (assistances);