alter table product add column item_code BIGINT not null default 0;

with tmp as (
		select ptmp.id,
				cast (substring(ptmp."name" from '\(арт.*(\d\d\d\d)\)') as bigint) as item_code,
				trim(both ' ' from substring(ptmp."name" from '(.*)\(арт.*\)')) as name_normal
			from shopbot.product ptmp where ptmp."name" like '%(арт%'
	)
update shopbot.product
    set "name" = tmp.name_normal, item_code = tmp.item_code
    from tmp where shopbot.product."id" = tmp.id;