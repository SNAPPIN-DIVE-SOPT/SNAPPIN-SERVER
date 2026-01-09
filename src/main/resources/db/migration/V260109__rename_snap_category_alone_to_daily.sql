ALTER TABLE product
    DROP CONSTRAINT IF EXISTS product_snap_category_check;

ALTER TABLE product
    ADD CONSTRAINT product_snap_category_check
        CHECK (
            snap_category IN (
                              'GRADUATION',
                              'WEDDING',
                              'COUPLE',
                              'DAILY',
                              'FAMILY',
                              'RECITAL'
                )
            );


ALTER TABLE portfolio
    DROP CONSTRAINT IF EXISTS portfolio_snap_category_check;

ALTER TABLE portfolio
    ADD CONSTRAINT portfolio_snap_category_check
        CHECK (
            snap_category IN (
                              'GRADUATION',
                              'WEDDING',
                              'COUPLE',
                              'DAILY',
                              'FAMILY',
                              'RECITAL'
                )
            );


ALTER TABLE photographer
    DROP CONSTRAINT IF EXISTS photographer_specialty_check;

ALTER TABLE photographer
    ADD CONSTRAINT photographer_specialty_check
        CHECK (
            specialty IN (
                          'GRADUATION',
                          'WEDDING',
                          'COUPLE',
                          'DAILY',
                          'FAMILY',
                          'RECITAL'
                )
            );
