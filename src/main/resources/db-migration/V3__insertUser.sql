-- Thêm dữ liệu mẫu vào bảng Users
INSERT INTO public.users (username, email, "password", status, create_date, role_id)
VALUES ('admin', 'admin@example.com', '$2a$10$3G/VueZy.i7Ur2PW3VtR0ueR4ERO0XI6KmCAlCuEekNt.w7RUQYFy', 'Active', '2023-08-15', 1);

INSERT INTO public.users (username, email, "password", status, create_date, role_id)
VALUES ('user1', 'user1@example.com', '$2a$10$3G/VueZy.i7Ur2PW3VtR0ueR4ERO0XI6KmCAlCuEekNt.w7RUQYFy', 'Active', '2023-08-15', 2);

INSERT INTO public.users (username, email, "password", status, create_date, role_id)
VALUES ('user2', 'user2@example.com', '$2a$10$3G/VueZy.i7Ur2PW3VtR0ueR4ERO0XI6KmCAlCuEekNt.w7RUQYFy', 'Active', '2023-08-15', 2);
