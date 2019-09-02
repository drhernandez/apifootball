create SCHEMA IF NOT EXISTS santex;

create table IF NOT EXISTS santex.COMPETITIONS
(
    id             bigint       not null
        primary key,
    area_name      varchar(255) null,
    code           varchar(255) null,
    fully_imported bit          null,
    name           varchar(255) null,
    constraint UK_3n695x4a2k7h7e2vykxg3ulq3
        unique (code)
);

create index idx_competition_code
    on santex.COMPETITIONS (code);

create table IF NOT EXISTS santex.PLAYERS
(
    id               bigint       not null
        primary key,
    country_of_birth varchar(255) null,
    date_of_birth    datetime     null,
    name             varchar(255) null,
    nationality      varchar(255) null,
    position         varchar(255) null
);

create table IF NOT EXISTS santex.TEAMS
(
    id         bigint       not null
        primary key,
    area_name  varchar(255) null,
    name       varchar(255) null,
    short_name varchar(255) null,
    tla        varchar(255) null
);

create table IF NOT EXISTS santex.COMPETITIONS_TEAMS
(
    competition_id bigint not null,
    team_id        bigint not null,
    primary key (competition_id, team_id),
    constraint FK37lrwdclist8kka70kxk78l3f
        foreign key (team_id) references TEAMS (id),
    constraint FKjdgb9wj71pnt6b8x501lm2wqf
        foreign key (competition_id) references COMPETITIONS (id)
);

create table IF NOT EXISTS santex.TEAMS_PLAYERS
(
    team_id   bigint not null,
    player_id bigint not null,
    primary key (team_id, player_id),
    constraint FK9w8h0gj4ufhd7dql1ktv448xp
        foreign key (team_id) references TEAMS (id),
    constraint FKoivhok0lm5f33759oo3elmlk6
        foreign key (player_id) references PLAYERS (id)
);
