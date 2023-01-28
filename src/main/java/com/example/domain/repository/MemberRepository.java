package com.example.domain.repository;

import com.example.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
//    private final EntityManager em;
//    public void save(Member member) {
//        em.persist(member);
//    }

    boolean existsByEmail(String email);

    Member getByEmail(String email);


//    public Member getBySocialId(String email) {return }
}
