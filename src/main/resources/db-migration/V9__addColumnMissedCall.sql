ALTER TABLE public.missed_call
ADD COLUMN direction varchar(50),
ADD COLUMN transaction_id varchar(255),
ADD COLUMN status boolean;
