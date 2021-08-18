CREATE TABLE public.objects
(
    obj_id integer NOT NULL,
    par_id integer,
    obj_type integer,
    obj_name character varying(512) COLLATE pg_catalog."default",
    obj_doc date,
    CONSTRAINT objects_pkey PRIMARY KEY (obj_id),
    CONSTRAINT objects_idtype_fkey FOREIGN KEY (obj_type)
        REFERENCES public.object_types (obj_t_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public.objects
    OWNER to postgres;