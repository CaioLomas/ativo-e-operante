CREATE TABLE public.imagens
(
    img_id BIGSERIAL PRIMARY KEY,
    img_name VARCHAR(70) UNIQUE NOT NULL,
    den_id INTEGER NOT NULL,
    CONSTRAINT fk_den FOREIGN KEY (den_id)
        REFERENCES public.denuncia(den_id) ON DELETE CASCADE
);
COMMIT;