<template>
  <div class="container my-5">
    <h1 class="my-4">게시판 - 수정</h1>
    <form @submit.prevent="updateBoard" enctype="multipart/form-data">
      <div class="card mb-3">
        <!-- 게시글 내용 -->
        <div class="card-header bg-transparent pb-0">
          <div class="d-flex justify-content-between">
            <input
              type="text"
              id="writer"
              name="writer"
              v-model="board.writer"
              class="form-control"
            />
            <input
              type="password"
              id="enteredPassword"
              v-model="enteredPassword"
              class="form-control"
            />
            <div class="d-flex">
              <p class="mb-0 me-4 mr-2">등록일시: {{ dateFormat(board.createdAt) }}</p>
              <p class="mb-0">수정일시: {{ dateFormat(board.modifiedAt) }}</p>
            </div>
          </div>
          <div class="d-flex justify-content-between pt-2">
            <h5 class="card-title mb-4">
              {{ this.board.categoryName }}:
              <input
                type="text"
                id="title"
                name="title"
                v-model="board.title"
                class="form-control"
              />
            </h5>
            <p class="card-text mb-4">조회수: {{ this.board.visitCount }}</p>
          </div>
        </div>
      </div>
      <div class="card-body border">
        <textarea
          id="content"
          name="content"
          v-model="board.content"
          class="form-control"
        ></textarea>
      </div>
      <br />

      <!-- 첨부파일  -->
      <div id="attachmentsList">
        <div
          v-for="(attachment, index) in attachments"
          :key="attachment.attachmentId"
          class="file-block"
        >
          <span>{{ attachment.originName }}</span>
          <a
            :href="`http://localhost:8080/attachment/download?attachmentId=${attachment.attachmentId}`"
            class="download-button"
            download
            >Download</a
          >
          <button
            @click="deleteAttachment(index, attachment.attachmentId)"
            class="delete-button"
          >
            X
          </button>
        </div>

        // TODO : computed 확인
        <div
          v-for="index in Math.max(maxAttachments - attachments.length, 0)"
          :key="index"
          class="file-block"
        >
          <input
            type="file"
            :id="'attachment' + (attachments.length + index + 1)"
            name="files"
            @change="handleFileChange($event)"
          />
        </div>
      </div>
      <br />

      <!-- 버튼그룹 -->
      <div class="d-flex justify-content-center mt-3">
        <div class="buttons">
          <router-link :to="clickCancleButton()" class="btn btn-secondary btn-block">
            취소
          </router-link>
          <div class="col-md-6">
            <button type="submit" class="btn btn-primary btn-block">저장</button>
          </div>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import { BOARD_VIEW_URL, BOARD_UPDATE_URL } from "../scripts/URLs.js";
import { api, multipartApi } from "../scripts/APICreate.js";
import moment from "moment";

export default {
  data() {
    return {
      enteredPassword: "", // 업데이트 시 비밀번호 확인
      //데이터베이스에서 가져온 게시글 정보
      board: {
        boardId: "",
        writer: "",
        password: "",
        createdAt: "",
        modifiedAt: "",
        categoryId: "",
        title: "",
        content: "",
        categoryName: "",
      },
      //게시판 검색 조건
      searchCondition: {
        currentPage: "",
        categoryId: "",
        searchText: "",
        startDate: "",
        endDate: "",
        pageSize: 10,
        offset: 0,
      },
      //수정될 게시글 내용
      formData: {
        categoryId: "",
        writer: "",
        password: "",
        confirmPassword: "",
        title: "",
        content: "",
        files: [],
      },
      attachments: [], //첨부파일 목록
      maxAttachments: 3,
      attachmentCounter: 0,
      deletedAttachmentIds: [], //삭제될 첨부파일 목록
    };
  },
  mounted() {
    //URL로부터 게시글ID와 검색조건 파싱
    //TODO : 한줄로 가능
    this.board.boardId = this.$route.query.boardId;
    this.searchCondition.currentPage = this.$route.query.currentPage;
    this.searchCondition.categoryId = this.$route.query.categoryId;
    this.searchCondition.searchText = this.$route.query.searchText;
    this.searchCondition.startDate = this.$route.query.startDate;
    this.searchCondition.endDate = this.$route.query.endDate;
    this.searchCondition.pageSize = this.$route.query.pageSize;
    this.searchCondition.offset = this.$route.query.offset;

    // 게시글 정보를 가져오는 API 호출
    this.fetchBoardData();
  },
  methods: {
    fetchBoardData() {
      // 게시글 정보를 가져오는 API 호출 및 데이터 할당
      const params = {
        boardId: this.board.boardId,
        categoryId: this.searchCondition.categoryId,
        searchText: this.searchCondition.searchText,
        startDate: this.searchCondition.startDate,
        endDate: this.searchCondition.endDate,
        currentPage: this.searchCondition.currentPage,
      };

      const requestURL = `${BOARD_VIEW_URL}?${Object.entries(params)
        .map(([key, value]) => `${key}=${value}`)
        .join("&")}`;
      console.log(requestURL);
      api
        .get(requestURL)
        .then((response) => {
          const responseData = response.data.data;
          Object.assign(this.board, responseData.board);
          Object.assign(this.attachments, responseData.attachments);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    dateFormat(date) {
      return moment(date).format("YYYY-MM-DD");
    },
    //취소버튼 클릭 시 게시글 상세 보기 페이지로 이동
    clickCancleButton() {
      return this.getBoardDetail(this.board.boardId);
    },
    updateBoard() {
      //Multipart FormData 전송을 위해 FormData 사용
      const summitFormData = new FormData();

      summitFormData.append("boardId", this.board.boardId);
      summitFormData.append("categoryId", this.board.categoryId);
      summitFormData.append("writer", this.board.writer);
      summitFormData.append("password", this.enteredPassword);
      summitFormData.append("title", this.board.title);
      summitFormData.append("content", this.board.content);

      //새로 추가 하려는 첨부 파일 목록
      this.formData.files.forEach((file) => {
        summitFormData.append("files", file);
      });
      //삭제되는 첨부파일 아이디
      summitFormData.append("deletedAttachmentIds", this.deletedAttachmentIds);

      for (const pair of summitFormData.entries()) {
        console.log(pair[0], pair[1]);
      }

      //게시글 수정 요청
      multipartApi
        .put(BOARD_UPDATE_URL, summitFormData)
        .then((response) => {
          alert(response.data.data);
          this.$router.push(this.getBoardDetail(this.board.boardId));
        })
        .catch((error) => {
          alert(error.message);
          console.log(error);
        });
    },
    //첨부파일 삭제 버튼 클릭 시 해당 첨부파일ID를 deletedAttachmentIds에추가
    deleteAttachment(index, attachmentId) {
      this.attachments.splice(index, 1);
      this.deletedAttachmentIds.push(attachmentId);
    },

    //첨부파일 추가 시
    handleFileChange(event) {
      // 선택한 파일
      const file = event.target.files[0];
      // formData에 파일 추가
      this.formData.files.push(file);
    },
    //검색조건과 함께 게시글 상세 보기 페이지로 이동하는 함수
    getBoardDetail(boardId) {
      const params = {
        boardId: boardId,
        categoryId: this.searchCondition.categoryId,
        searchText: this.searchCondition.searchText,
        startDate: this.searchCondition.startDate,
        endDate: this.searchCondition.endDate,
        currentPage: this.searchCondition.currentPage,
        pageSize: this.searchCondition.pageSize,
        offset: this.searchCondition.offset,
      };
      return `${BOARD_VIEW_URL}?${Object.entries(params)
        .map(([key, value]) => `${key}=${value}`)
        .join("&")}`;
    },
  },
};
</script>

<style scoped>
.error-message {
  color: red;
}
</style>
