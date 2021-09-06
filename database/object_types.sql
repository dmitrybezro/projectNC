CREATE TABLE public.object_types
(
    obj_t_id integer NOT NULL,
    list_attr character varying(512) COLLATE pg_catalog."default",
    CONSTRAINT object_types_pkey PRIMARY KEY (obj_t_id)
)

TABLESPACE pg_default;

ALTER TABLE public.object_types
    OWNER to postgres;