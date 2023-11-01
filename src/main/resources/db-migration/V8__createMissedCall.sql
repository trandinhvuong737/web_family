CREATE TABLE public.missed_call
(
    call_id            serial  NOT NULL,
    source_number      varchar null,
    destination_number varchar NULL,
    created_date       bigint    NULL,
    CONSTRAINT missed_call_pk PRIMARY KEY (call_id)
);
