CREATE TABLE public."values"
(
    obj_id integer NOT NULL,
    attr_id integer NOT NULL,
    param_val character varying COLLATE pg_catalog."default",
    CONSTRAINT values_pkey PRIMARY KEY (obj_id, attr_id),
    CONSTRAINT values_attr_id_fkey FOREIGN KEY (attr_id)
        REFERENCES public.attributes (attr_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT values_obj_id_fkey FOREIGN KEY (obj_id)
        REFERENCES public.objects (obj_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE public."values"
    OWNER to postgres;