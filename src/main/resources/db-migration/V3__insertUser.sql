INSERT INTO product (product_name, unit_price, weight, transport_fee)
VALUES
    ('all',0,0,0);

-- Thêm dữ liệu mẫu vào bảng Users
INSERT INTO public.users (username, email, "password", status, create_date, role_id, product_id )
VALUES
    ('admin', 'admin@example.com', '$2a$10$3G/VueZy.i7Ur2PW3VtR0ueR4ERO0XI6KmCAlCuEekNt.w7RUQYFy', 'Active','2023-08-15', 1, 1),
    ('super admin', 'superadmin@example.com', '$2a$10$3G/VueZy.i7Ur2PW3VtR0ueR4ERO0XI6KmCAlCuEekNt.w7RUQYFy','Active', '2023-08-15', 1, 1),
    ('admin2', 'admin2@example.com', '$2a$10$3G/VueZy.i7Ur2PW3VtR0ueR4ERO0XI6KmCAlCuEekNt.w7RUQYFy', 'Active','2023-08-15', 2, 1);
