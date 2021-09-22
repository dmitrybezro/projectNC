CREATE TABLE public.objects
(
    obj_id integer NOT NULL,
    par_id integer,
    obj_name character varying(512) COLLATE pg_catalog."default",
    obj_doc date,
    CONSTRAINT objects_pkey PRIMARY KEY (obj_id)
)

TABLESPACE pg_default;

ALTER TABLE public.objects
    OWNER to postgres;