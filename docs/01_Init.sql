SET search_path TO stock_finder, public;

drop table if exists stock_history cascade;
commit;

drop index if exists stock_finder_perc_1yr;
drop index if exists stock_finder_perc_2yr;
drop index if exists stock_finder_perc_5yr;
commit;

drop schema if exists stock_finder;
commit;

create schema if not exists stock_finder;
commit;

create table stock_history (
    sym varchar not null,
    name varchar not null,
    sector varchar not null,
    industry varchar not null,
    perc_1yr numeric,
    std_1yr numeric,
    perc_2yr numeric,
    std_2yr numeric,
    perc_5yr numeric,
    std_5yr numeric,
    samples integer,
    processed boolean default false,
    updated_on timestamp,
    primary key (sym)
);

create index stock_finder_perc_1yr on stock_history(perc_1yr);
create index stock_finder_perc_2yr on stock_history(perc_2yr);
create index stock_finder_perc_5yr on stock_history(perc_5yr);
commit;