# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table pictures (
  id                            bigserial not null,
  name                          varchar(255),
  type                          varchar(255),
  url                           varchar(255),
  published                     timestamptz,
  post_id                       bigint,
  constraint pk_pictures primary key (id)
);

create table posts (
  id                            bigserial not null,
  title                         varchar(255),
  description                   varchar(16384),
  published                     timestamptz,
  user_id                       bigint,
  constraint pk_posts primary key (id)
);

create table users (
  id                            bigserial not null,
  name                          varchar(255) not null,
  surname                       varchar(255) not null,
  email                         varchar(255) not null,
  password                      varchar(255),
  auth_token                    varchar(255),
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id)
);

create index ix_pictures_post_id on pictures (post_id);
alter table pictures add constraint fk_pictures_post_id foreign key (post_id) references posts (id) on delete restrict on update restrict;

create index ix_posts_user_id on posts (user_id);
alter table posts add constraint fk_posts_user_id foreign key (user_id) references users (id) on delete restrict on update restrict;


# --- !Downs

alter table if exists pictures drop constraint if exists fk_pictures_post_id;
drop index if exists ix_pictures_post_id;

alter table if exists posts drop constraint if exists fk_posts_user_id;
drop index if exists ix_posts_user_id;

drop table if exists pictures cascade;

drop table if exists posts cascade;

drop table if exists users cascade;

