CREATE TABLE public.attributes
(
    attr_id integer NOT NULL,
    attr_name character varying(512) COLLATE pg_catalog."default",
    CONSTRAINT attributes_pkey PRIMARY KEY (attr_id)
)

TABLESPACE pg_default;

ALTER TABLE public.attributes
    OWNER to postgres;