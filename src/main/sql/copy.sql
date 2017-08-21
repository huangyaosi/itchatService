insert into public.share_note(idid, creation_date, modification_date, nick_name, text) values('12121212121212121212121','2017-07-31 10:37:04.602088','2017-07-31 10:37:04.602088','Enosh~Huang','good day')

#update share note with relate user id
update public.share_note s set user_id=U.user_id
from (
	select u.id as user_id, sn.nick_name as nick_name from public.share_note sn, public.users u where sn.nick_name = u.username
) U
where s.nick_name = U.nick_name;