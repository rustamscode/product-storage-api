INSERT INTO products (id, name, product_number, info, category, price,
                      amount, last_amount_update, creation_time, version)
SELECT gen_random_uuid(),
       'Product ' || s.i::text,
       (s.i % 1000000) + 1,
       'Information about product ' || s.i::text,
       CASE (s.i % 5)
           WHEN 0 THEN 'FOOD'
           WHEN 1 THEN 'CLOTHING'
           WHEN 2 THEN 'ELECTRONICS'
           WHEN 3 THEN 'HOME_APPLIANCES'
           WHEN 4 THEN 'BOOKS'
           WHEN 5 THEN 'TOYS_AND_GAMES'
           ELSE 'BEAUTY_AND_CARE'
           END,
       random() * 100,
       (s.i % 100) * 10,
       current_timestamp,
       current_date,
       random() * 100

FROM generate_series(1, 1000000) s(i);