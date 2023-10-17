CREATE TABLE public.role
(
    role_id         serial    NOT NULL,
    role_name       varchar    NULL ,
    description     text       NULL ,
    CONSTRAINT role_pk PRIMARY KEY (role_id)
);

CREATE TABLE public.users
(
    user_id      serial    NOT NULL,
    username    varchar NULL,
    email       varchar NULL,
    "password"  varchar NULL,
    status      varchar NULL,
    create_date date    NULL,
    role_id     INT NULL ,
    CONSTRAINT users_pk PRIMARY KEY (user_id),
    CONSTRAINT users_role_id_fk FOREIGN KEY (role_id) REFERENCES public.role (role_id)
);


CREATE TABLE public.orders
(
    order_id      serial    NOT NULL ,
    user_id        int    NOT NULL ,
    create_at     timestamp NOT NULL ,
    customer      varchar NULL ,
    address       varchar NULL ,
    phone_number  varchar NULL ,
    product       varchar NOT NULL ,
    quantity      int NOT NULL ,
    status        varchar NULL ,
    CONSTRAINT oder_pk PRIMARY KEY (order_id),
    CONSTRAINT oder_use_id_fk FOREIGN KEY (user_id) REFERENCES public.users (user_id)
);

CREATE TABLE public.product
(
    product_id serial NOT NULL ,
    product_name varchar NULL ,
    unit_price bigint NULL ,
    weight int NULL ,
    CONSTRAINT product_pk PRIMARY KEY (product_id)
);

CREATE TABLE IF NOT EXISTS public.privileges
(
    privilege_id          serial NOT NULL ,
    name        varchar(255),
    description TEXT,
    screen_key  varchar(255),
    CONSTRAINT privileges_pk PRIMARY KEY (privilege_id)
);

CREATE TABLE IF NOT EXISTS public.roles_privileges
(
    role_id      INT,
    privilege_id INT,
    PRIMARY key (role_id, privilege_id),
    CONSTRAINT fk_roles_privileges_roles_role_id FOREIGN KEY (role_id) REFERENCES public.role (role_id),
    CONSTRAINT fk_roles_privileges_privilege_id_privileges_id FOREIGN KEY (privilege_id) REFERENCES public.privileges (privilege_id)
);
