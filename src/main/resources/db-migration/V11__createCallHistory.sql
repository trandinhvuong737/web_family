DROP TABLE public.missed_call;

CREATE TABLE public.call_history
(
    call_id        serial NOT NULL,
    hotline_number varchar NULL,
    caller_phone   varchar NULL,
    callee_phone   varchar NULL,
    created_date   bigint NULL,
    type_call      varchar NULL,
    disposition    varchar NULL,
    status boolean NOT NULL,
    CONSTRAINT call_history_pk PRIMARY KEY (call_id)
);
