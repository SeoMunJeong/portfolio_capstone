# PersonalTrainer
OCR 기반, 인바디 이미지 인식에 따른 개인맞춤형 식단표 제공 서비스<br>
(Personalized meal table service with InBody result image recognition based on AI)

>[프로젝트 소개 ppt](https://docs.google.com/presentation/d/1-GnFoM7eYvnUloaMjpYrnfx5up-jsp-T-9BWabT4Ygs/edit?usp=sharing)

## 🛠 Tech Stack 🛠
<img src="https://img.shields.io/badge/Java-007396?style=flat&logo=Java&logoColor=white"/></a>
<img src="https://img.shields.io/badge/PHP-777BB4?style=flat&logo=PHP&logoColor=white"/></a>
<a href="https://www.mysql.com/" target="_blank"><img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"/></a>
<a href="https://aws.amazon.com/ko/" target="_blank"><img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat&logo=AmazonAWS&logoColor=white"/></a>
<img src="https://img.shields.io/badge/github-181717?style=flat&logo=github&logoColor=white"></a>

## 🖥️ Introduce our project
이미지 또는 자연어 인식 기술을 통해 인바디 결과지를 자동으로 인식한 후, <br>
이를 바탕으로 사용자의 체형을 분석하고 개인맞춤형 식단표를 제공해주는 서비스
<br>

### ❔ Target User
![image](https://user-images.githubusercontent.com/89902489/199339555-de108da1-0c73-46f8-8faf-12e851323b08.png)
<br>
PT를 직접 받을 때는 개인의 인바디 측정 결과를 바탕으로 식단표를 제공받아 운동계획을 세우지만, 개인이 직접 식단을 짜는 데에는 한계가 있다. <br>
Personal Trainer은 비용을 지불하고 PT를 직접 받지 않더라도 집에서도 균형 있는 식사로 영양분을 섭취할 수 있도록 도와줄 수 있다. 더불어 사용자에게 개인 맞춤형 식단표를 제공해주는 서비스를 통해, 사용자는 PT 비용을 절감하고 건강식 정보와 식단표를 쉽고 간편하게 제공받을 수 있다. 나아가 본인에게 필요한 영양소를 직접 알아보고 정기적으로 식단 계획을 세워 꾸준히 섭취하는 것과 본인 체형에 맞는 적절한 운동을 찾아보는 것이 번거롭고, 쉽지 않은 일이라고 느끼는 사람들에게 편의를 제공하고자 한다.


### 🕰️ Develop Period
* 22.04.01 - 22.10.31
![image](https://user-images.githubusercontent.com/89902489/199335382-db12c2e4-9574-4911-9d25-3966071f1c84.png)
<br>

### 🧑‍🤝‍🧑 Organization
![image](https://user-images.githubusercontent.com/89902489/200104227-841de55a-350f-432c-91a4-5d024177fcb7.png)

### ✔️ Used Technique
| Full Stack | Technique | Role Officer |
| :--------------------------: | :-----------------------------------------------: | :------------------------------: |
| <center> Front-End </center> | <center> Android Studio </center> | <center> 이예진, 서문정, 이수화 </center> |
| <center>  Back-End </center> | <center> PHP </center> | <center> 유민영, 전수민 </center> |

### ⚙️ Develop Environment
- `Java 8`
- `JDK 16.0.2`
- **Database** : phpmyadmin
- **Server** : AWS EC2 ubuntu

### 📌 Business Model Canvas
![image](https://user-images.githubusercontent.com/92795889/211075472-13e4c464-26f6-4da7-bf98-d7b97a9e3e89.png)
<br>

### 📌 Development Skills
##### 안드로이드 앱 구현 기술
- 사용자 중심의 UI/UX를 위한 안드로이드 기반 애플리케이션 구현 기술
##### 이미지 텍스트 인식 기술
- NaverClova OCR API를 통한 인바디 결과 용지 속 텍스트를 찾아내어 추출하는 기술
##### MySQL
- 개인 맞춤형 식단표 추천 기술에 필요한 인바디 DB, 식품DB, 식단 DB를 MySQL을 사용하여 구축함

### 📌 Development Tools
|           Tools           |       Explanation       |
| :----------------------: | :-------------------------: |
| <center> Android Studio </center> | <center>  안드로이드 운영 체제를 사용하여 식단표, 일정 관리, 데이터베이스 등 사용자 중심의 UI/UX를 위한 안드로이드 기반 애플리케이션 구현 </center> |
| <center> MySQL </center> | <center> 개인 맞춤형 식단표 추천 기술에 필요한 인바디 DB, 식단 DB를 구축 </center> |
| <center> PHP </center> | <center>  MySQL의 DB 수정, 삭제 등 PHP 기반의 백엔드 개발 환경  </center> |
| <center> AWS EC2 </center> | <center>  AWS의 EC2를 테스트 서버로 사용 </center> |

## 📋 Technical Architecture
![서비스 구성도](https://user-images.githubusercontent.com/113801496/199249384-258ee377-3db1-4757-9351-5aaaf24f9236.PNG)

## 📎 Deliverables
> [PersonalTrainer-Demo영상](https://youtu.be/ADlf3PDPBUY)

![mvp](https://user-images.githubusercontent.com/113801496/199274798-91a9d083-9eea-4bd0-bf24-6ea7011ce9f8.PNG)
![ocr 실행화면](https://user-images.githubusercontent.com/113801496/199274851-cc2a6b11-5ad9-41bc-849c-b80d8087c482.PNG)
