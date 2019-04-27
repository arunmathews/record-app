CREATE TABLE "record" (
"id"                 SERIAL PRIMARY KEY,
"last_name"          VARCHAR(300) NOT NULL,
"first_name"         VARCHAR(300) NOT NULL,
"gender"             VARCHAR(300) NOT NULL,
"favorite_color"     VARCHAR(300) NOT NULL,
"birth_date"         DATE NOT NULL
);

CREATE INDEX "record_idx1" ON "record"("last_name");

CREATE INDEX "record_idx2" ON "record"("gender");

CREATE INDEX "record_idx3" ON "record"("birth_date");