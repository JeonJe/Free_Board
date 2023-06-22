<template>
  <div class="container my-5">
    <h1 class="my-4">게시판 - 보기</h1>
    <!-- 비밀번호 확인 모달  -->
    <div v-if="showPasswordModal" class="modal">
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">비밀번호 확인</h5>
            <button
              type="button"
              class="close"
              data-dismiss="modal"
              @click="hidePasswordModal()"
            >
              &times;
            </button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="validatePasswordAndAction">
              <input type="password" name="enteredPassword" v-model="password" required />
              <br />
              <div v-if="passwordError" class="error-message">{{ passwordError }}</div>
              <br />
              <button type="submit" class="btn btn-primary">확인</button>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- 게시글 내용 -->
    <div class="card mb-3">
      <div class="card-header bg-transparent pb-0">
        <div class="d-flex justify-content-between">
          <p class="mb-0">작성자: {{ board.writer }}</p>
          <div class="d-flex">
            <p class="mb-0 me-4 mr-2">등록일시: {{ dateFormat(board.createdAt) }}</p>
            <p class="mb-0">수정일시: {{ dateFormat(board.modifiedAt) }}</p>
          </div>
        </div>
        <div class="d-flex justify-content-between pt-2">
          <h5 class="card-title mb-4">
            {{ this.board.categoryName }}: {{ board.title }}
          </h5>
          <p class="card-text mb-4">조회수: {{ this.board.visitCount }}</p>
        </div>
      </div>
    </div>
    <div class="card-body border">
      <div class="card-text">내용 : {{ board.content }}</div>
    </div>
    <!-- 첨부파일  -->
    <a
      v-for="attachment in attachments"
      :key="attachment.attachmentId"
      :href="`http://localhost:8080/attachment/download?attachmentId=${attachment.attachmentId}`"
      class="mb-2 d-block"
      download
    >
      {{ attachment.originName }}<br />
    </a>
    <!-- 댓글 -->
    <div>
      <div
        v-if="comments && comments.length > 0"
        class="list-group comment-item bg-light"
      >
        <div
          v-for="comment in comments"
          :key="comment.createdAt"
          class="list-group-item comment-item"
        >
          <div class="d-flex justify-content-between">
            <small class="mb-1">{{ dateFormat(comment.createdAt) }}</small>
          </div>
          <p class="mb-1">{{ comment.content }}</p>
        </div>
      </div>
      <p v-else>댓글이 없습니다.</p>
    </div>
    <!-- 댓글 추가 -->
    <div class="d-flex justify-content-center my-3">
      <form @submit.prevent="addComment" class="w-75">
        <div class="row">
          <div class="col-8">
            <div class="form-group">
              <textarea
                class="form-control"
                v-model="commentContent"
                rows="4"
                placeholder="댓글을 입력해주세요."
              ></textarea>
            </div>
          </div>
          <div class="col-4 d-flex align-items-center">
            <button type="submit" class="btn btn-primary">댓글 등록</button>
          </div>
        </div>
      </form>
    </div>
    <div></div>

    <!-- 버튼그룹 -->
    <div class="d-flex justify-content-center mt-3">
      <div class="buttons">
        <router-link :to="clickCancleButton()" class="btn btn-secondary btn-block">
          취소
        </router-link>
        <button class="btn btn-primary" @click="handlePasswordModal('edit')">수정</button>
        <button class="btn btn-primary" @click="handlePasswordModal('delete')">
          삭제
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import {
  BOARD_LIST_URL,
  BOARD_VIEW_URL,
  BOARD_DELETE_URL,
  BOARD_ADD_COMMENT_URL,
  BOARD_UPDATE_URL,
} from "../scripts/URLs.js";
import { api } from "../scripts/APICreate.js";
import moment from "moment";

export default {
  data() {
    return {
      showPasswordModal: false,
      isEdit: false,
      password: '', // 수정 시 비밀번호 확인용
      passwordError: '', 
      //데이터베이스에서 가져온 게시글 정보
      board: {
        boardId: '',
        writer: '',
        password: '',
        createdAt: '',
        modifiedAt: '',
        categoryId: '',
        title: '',
        content: '',
        visitCount: '',
        categoryName: '',
      },
      attachments: [], //첨부파일 목록
      comments: [], //댓글 목록
      commentContent: '', //댓글내용
      //게시판 검색 조건
      searchCondition: {
        currentPage: '',
        categoryId: '',
        searchText: '',
        startDate: '',
        endDate: '',
        pageSize: 10,
        offset: 0,
      },
    };
  },
  mounted() {
    //URL로부터 게시글ID와 검색조건 파싱
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

      api
        .get(requestURL)
        .then((response) => {
          const responseData = response.data.data;
          //TODO : = 로 사용해도 테스트
          Object.assign(this.board, responseData.board);
          Object.assign(this.comments, responseData.comments);
          Object.assign(this.attachments, responseData.attachments);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    //비밀번호 확인
    validatePasswordAndAction() {
      const hashedPassword = this.$CryptoJS.SHA256(this.password).toString();

      if (hashedPassword === this.board.password) {
        this.hidePasswordModal();
        //수정버튼 클릭 시 검색어 조건과 함께 수정 페이지로 이동
        if (this.isEdit) {
          this.$router.push({
            path: BOARD_UPDATE_URL,
            query: {
              boardId: this.board.boardId,
              categoryId: this.searchCondition.categoryId,
              searchText: this.searchCondition.searchText,
              startDate: this.searchCondition.startDate,
              endDate: this.searchCondition.endDate,
              currentPage: this.searchCondition.currentPage,
              pageSize: this.searchCondition.pageSize,
              offset: this.searchCondition.offset,
            },
          });

          //삭제 버튼 클릭 시 삭제 요청
        } else {
          try {
            //TODO :  await async
            api
              .delete(BOARD_DELETE_URL, {
                boardId: this.board.boardId,
                password: this.password,
              })
              .then((response) => {
                const responseData = response.data.data;
                alert(responseData.data);
                this.$router.replace({ path: "/board/list", query: {} });
              })
              .catch((error) => {
                console.log(error);
              });
          } catch (error) {
            alert(error);
          }
        }
      } else {
        this.passwordError = "비밀번호가 일치하지 않습니다.";
      }
    },
    //댓글 추가
    addComment() {
      api
        .post(BOARD_ADD_COMMENT_URL, {
          content: this.commentContent,
          boardId: this.board.boardId,
        })
        .then((response) => {
          const responseData = response.data.data;
          this.comments = responseData.comments;
          this.commentContent = "";
        })
        .catch((error) => {
          console.log(error);
        });
    },

    hidePasswordModal() {
      this.showPasswordModal = false;
    },
    handlePasswordModal(action) {
      this.showPasswordModal = true;
      this.isEdit = action === "edit";
    },
    dateFormat(date) {
      return moment(date).format("YYYY-MM-DD");
    },
    //취소 버튼 클릭 시 검색어 조건과 함께 리스트 페이지로 이동
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

<style scoped>
.error-message {
  color: red;
}
</style>
