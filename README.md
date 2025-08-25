---
layout: page
title: Guideline
permalink: /Guideline/
---


## 신세계아이앤씨 9차수 3조 데일리 스크럼
> 데일리 스크럼 작성 예시


### 블로그 주소
> https://ssg-9-3-dailyscrum.github.io/


### 깃허브 연동
#### 1. 로컬 저장소와 깃허브 저장소 연동

`git clone https://github.com/ssg-9-3-dailyscrum/ssg-9-3-dailyscrum.github.io.git`


#### 2. 초기 설정

```
cd [로컬 저장소 경로]  
git checkout main
git pull origin main
```
예시:

`cd c/study/ssg-9-3-dailyscrum.github.io`


#### 3. 브랜치 생성

```
git checkout -b [브랜치이름] // 브랜치 생성과 동시에 이동
git branch // 현재 브랜치 위치 확인 명령어 (본인껄로 설정되어 있어야 함)
```
예시:

`git checkout -b SGL`


#### 4. 스크럼 작성 후 올리기
> _posts/에 스크럼 작성 (자세한건 아래 새 스크럼 작성 참고)

작성 후 본인 브랜치 깃허브 저장소에 올리기
```
git add YYYY-MM-DD-이름.md
git commit -m "YYMMDD 이름"
git push origin 본인브랜치
```
예시:
```
git add 2025-08-24.송규림.md
git commit -m "250824 SGL"
git push origin SGL
```


#### 5. PR(풀 리퀘스트) : 작성한 내용을 메인 브랜치에 합치기 위해 반드시 해야하는 작업
- 깃허브에서 Pull requests

![alt text](/public/img/screenshot-1.png)

- New pull request 

![alt text](/public/img/screenshot-2.png)

- base repository를 ssg-9-3-dailyscrum/ssg-9-3-dailyscrum.github.io로 변경

![alt text](/public/img/screenshot-3.png)

- base를 main으로, compare을 본인 브랜치로 설정 후 Create Pull Request

![alt text](/public/img/screenshot-4.png)

- title 및 decription 작성 후 Create Pull Request

![alt text](/public/img/screenshot-5.png)

- 파일 변경 내역 확인 및 병합 요청

> 본인 브랜치에서 어떤 파일들이 변경되었는지 'Files changed' 탭에서 확인 후, 변경 사항에 문제가 없다고 판단되면 병합을 요청

- 팀원 검토 및 승인

> 다른 팀원 중 한 명이 PR을 확인하고, 변경 사항에 문제가 없으면 승인(Approve), 승인이 완료되면 메인 브랜치(main)와 자동으로 병합


#### 6. 로컬 저장소 최신화
```
git checkout main
git pull origin main
git checkout 본인브랜치
```
충돌이 발생하면 충돌 해결하고 `git merge main`


> 4 ~ 6 작업 반복

### 새 스크럼 작성

#### 1. 파일명
> _posts/YYYY-MM-DD-작성자이름.md

예시:

`_posts/2025-08-24-송규림.md`

#### 2. 메타데이터
YYYY-MM-DD-이름.md 파일 맨 위에 작성
```
---
layout: post
title: "DailyScrum" # 제목
date: 2025-08-24 # YYYY-MM-DD 형식 (파일명과 일치해야 함)
author: "송규림" # 본인 이름으로 변경
---
```
#### 3. 마크다운
```
## 📝 오늘 한 일

- 운영체제 공부
- 깃허브 IO 생성

## 🎯 내일 할 일

- CLI 리팩토링

## 💭 한줄평

- 열심히 해보자

## 🔗 공유하고 싶은 것

- 공유
```

> ※ 스크럼 작성할 때는 반드시 main 에서 pull 받아온 후 진행해주세요 !!


