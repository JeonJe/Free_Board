<template>
  <div class="container my-4">
    <h1 class="my-4">게시판 - 등록</h1>
    <div class="row justify-content-center">
      <div class="col-md-12 bg-light">
        <!-- 폼  -->
        <form @submit.prevent="saveBoardInfo" enctype="multipart/form-data">
          <div class="form-group row border-bottom p-3">
            <label
              for="category_id"
              class="col-sm-2 col-form-label d-flex align-items-center"
              >카테고리:</label
            >
            <div class="col-sm-8">
              <!-- 카테고리 -->
              <select
                id="category_id"
                name="category_id"
                class="form-control"
                required
                v-model="formData.categoryId"
              >
                <option
                  v-for="category in categories"
                  :value="category.categoryId"
                  :key="category.categoryId"
                >
                  {{ category.categoryName }}
                </option>
              </select>
            </div>
          </div>
          <!-- 작성자 -->
          <div class="form-group row border-bottom p-3">
            <label for="writer" class="col-sm-2 col-form-label d-flex align-items-center"
              >작성자:</label
            >
            <div class="col-sm-8">
              <input
                autofocus
                type="text"
                id="writer"
                name="writer"
                class="form-control"
                required
                v-model="formData.writer"
              />
            </div>
          </div>
          <!-- 비밀번호 & 비밀번호 확인 -->
          <div class="form-group row border-bottom p-3">
            <label
              for="password"
              class="col-sm-2 col-form-label d-flex align-items-center"
              >비밀번호:</label
            >
            <div class="col-sm-4">
              <input
                type="password"
                id="password"
                name="password"
                placeholder="비밀번호"
                class="form-control"
                required
                v-model="formData.password"
              />
            </div>

            <div class="col-sm-4">
              <input
                type="password"
                id="confirmPassword"
                name="confirmPassword"
                placeholder="비밀번호 확인"
                class="form-control"
                required
                v-model="formData.confirmPassword"
              />
            </div>
          </div>
          <!-- 제목  -->
          <div class="form-group row border-bottom p-3">
            <label for="title" class="col-sm-2 col-form-label d-flex align-items-center"
              >제목:</label
            >
            <div class="col-sm-8">
              <input
                type="text"
                id="title"
                name="title"
                class="form-control"
                required
                v-model="formData.title"
              />
            </div>
          </div>
          <!-- 내용 -->
          <div class="form-group row border-bottom p-3">
            <label for="content" class="col-sm-2 col-form-label d-flex align-items-center"
              >내용:</label
            >
            <div class="col-sm-8">
              <textarea
                id="content"
                name="content"
                class="form-control"
                rows="6"
                required
                v-model="formData.content"
              ></textarea>
            </div>
          </div>
          <!-- 첨부파일 -->
          <div class="form-group row p-3">
            <label
              for="attachment1"
              class="col-sm-2 col-form-label d-flex align-items-center"
              >첨부파일:</label
            >
            <div class="col-sm-8">
              <input
                type="file"
                id="attachment1"
                name="files"
                class="form-control-file mb-2"
                @change="handleFileChange($event)"
              />
              <input
                type="file"
                id="attachment2"
                name="files"
                class="form-control-file mb-2"
                @change="handleFileChange($event)"
              />
              <input
                type="file"
                id="attachment3"
                name="files"
                class="form-control-file mb-2"
                @change="handleFileChange($event)"
              />
            </div>
          </div>
          <!-- 버튼 그룹 -->
          <div class="row mt-3 justify-content-center">
            <div class="col-md-6">
              <router-link :to="clickCancleButton()" class="btn btn-secondary btn-block">
                취소
              </router-link>
            </div>
            <div class="col-md-6">
              <button type="submit" class="btn btn-primary btn-block">저장</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { api, multipartApi } from "../scripts/APICreate.js";
import { BOARD_LIST_URL, CATEOGRY_LIST_URL, BOARD_SAVE_URL } from "../scripts/URLs.js";

export default {
  data() {
    return {
      // 검색 조건
      searchCondition: {
        currentPage: "",
        category: "",
        searchText: "",
        startDate: "",
        endDate: "",
        pageSize: "",
        offset: "",
      },
      // 게시글 정보
      formData: {
        categoryId: "",
        writer: "",
        password: "",
        confirmPassword: "",
        title: "",
        content: "",
        files: [],
      },
      categories: [],
    };
  },
  //컴포넌트 인스턴스 생성전에 호출하여 검색조건 초기화 작업 수행
  beforeRouteEnter(to, from, next) {
    const searchCondition = {
      currentPage: to.query.currentPage || "",
      categoryId: to.query.categoryId || "",
      searchText: to.query.searchText || "",
      startDate: to.query.startDate || "",
      endDate: to.query.endDate || "",
      pageSize: to.query.pageSize || 10,
      offset: to.query.offset || 0,
    };
    next((vm) => {
      vm.searchCondition = searchCondition;
    });
  },
  mounted() {
    this.getCategories();
  },

  methods: {
    //서버에서 카테고리 목록을 받아옴
    getCategories() {
      api
        .get(CATEOGRY_LIST_URL)
        .then((response) => {
          const data = response.data;
          this.categories = data.data;
        })
        .catch((error) => {
          console.error("Error:", error);
        });
    },
    //첨부파일 추가 시
    handleFileChange(event) {
      // 선택한 파일
      const file = event.target.files[0];

      // formData에 파일 추가
      this.formData.files.push(file);
    },
    //작성한 게시글 정보를 저장하는 함수
    saveBoardInfo() {
      if (this.formData.writer.length < 3 || this.formData.writer.length >= 5) {
        alert("작성자는 3글자 이상 5글자 미만이어야 합니다.");
        return;
      }
      if (
        this.formData.password.length < 4 ||
        this.formData.password.length >= 16 ||
        !/[A-Za-z0-9_$@#%&*]+/.test(this.formData.password) ||
        this.formData.password !== this.formData.confirmPassword
      ) {
        alert(
          "비밀번호는 4글자 이상 16글자 미만이어야 하며, 영문, 숫자, 특수문자를 포함해야 하며, 비밀번호 확인과 일치해야 합니다."
        );
        return;
      }

      if (this.formData.title.length < 4 || this.formData.title.length >= 100) {
        alert("제목은 4글자 이상 100글자 미만이어야 합니다.");
        return;
      }

      if (this.formData.content.length < 4 || this.formData.content.length >= 2000) {
        alert("내용은 4글자 이상 2000글자 미만이어야 합니다.");
        return;
      }
      //Multipart FormData 전송을 위해 FormData 사용
      const summitFormData = new FormData();

      summitFormData.append("categoryId", this.formData.categoryId);
      summitFormData.append("writer", this.formData.writer);
      summitFormData.append("password", this.formData.password);
      summitFormData.append("confirmPassword", this.formData.confirmPassword);
      summitFormData.append("title", this.formData.title);
      summitFormData.append("content", this.formData.content);

      this.formData.files.forEach((file) => {
        summitFormData.append(`files`, file);
      });

      //게시글 저장 요청
      multipartApi
        .post(BOARD_SAVE_URL, summitFormData)
        .then((response) => {
          alert(response.data.data);
          this.$router.replace({ path: BOARD_LIST_URL, query: {} });
        })
        .catch((error) => {
          alert(error.response.data.message);
          console.error("Error:", error);
        });
    },
    //취소버튼 클릭 시 검색 조건과 함께 리스트 페이지 이동
    clickCancleButton() {
      const params = {
        categoryId: this.searchCondition.categoryId,
        searchText: this.searchCondition.searchText,
        startDate: this.searchCondition.startDate,
        endDate: this.searchCondition.endDate,
        currentPage: this.searchCondition.currentPage,
        pageSize: this.searchCondition.pageSize,
        offset: this.searchCondition.offset,
      };

      const listPage = `${BOARD_LIST_URL}?${Object.entries(params)
        .map(([key, value]) => `${key}=${value}`)
        .join("&")}`;
      return listPage;
    },
  },
};
</script>
