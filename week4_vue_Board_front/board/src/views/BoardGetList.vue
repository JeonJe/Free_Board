<template>
  <div class="container">
    <h1 class="my-4">자유 게시판 목록</h1>

    <!-- 검색 조건 입력 폼  -->
    <form class="form-inline mb-4" @submit.prevent="searchBoardsAction">
      <input type="date" id="startDate" name="startDate" class="form-control mr-2" v-model="searchCondition.startDate" />

      <input type="date" id="endDate" name="endDate" class="form-control mr-2" v-model="searchCondition.endDate" />
      <!-- 카테고리 -->
      <select id="category" name="categoryId" class="form-control mr-2" v-model="searchCondition.categoryId">
        <option value="0">전체 카테고리</option>
        <option v-for="category in categories" :value="category.categoryId" :key="category.categoryId"
          :selected="category.categoryId === searchCondition.categoryId">
          {{ category.categoryName }}
        </option>
      </select>
      <!-- 검색어  입력 -->
      <input type="text" id="searchText" name="searchText" class="form-control mr-2" placeholder="카테고리 + 제목 + 내용"
        v-model="searchCondition.searchText" />

      <button type="submit" class="btn btn-primary">검색</button>
    </form>
    <!-- 총 조회 건수 -->
    <p>총 {{ this.totalCount }} 건</p>

    <!-- 게시글 리스트 -->
    <table class="table table-striped text-center">
      <thead class="text-center">
        <tr>
          <th>카테고리</th>
          <th>제목</th>
          <th>작성자</th>
          <th>조회수</th>
          <th>등록일시</th>
          <th>수정일시</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(boardItem) in searchBoards" :key="boardItem.boardId">
          <td>{{ getCategoryName(boardItem.categoryId) }}</td>
          <router-link :to="getBoardDetail(boardItem.boardId, boardItem.categoryId)">
            {{ boardItem.title }}
          </router-link>
          <td>{{ boardItem.writer }}</td>
          <td>{{ boardItem.visitCount }}</td>
          <td>{{ dateFormat(boardItem.createdAt) }}</td>
          <td>{{ dateFormat(boardItem.modifiedAt) }}</td>
        </tr>
      </tbody>
    </table>

    <!-- 페이지네이션 -->
    <div class="d-flex justify-content-between">
      <div class="text-center mx-auto">
        <a v-if="searchCondition.currentPage > 1" @click="getPageData(searchCondition.currentPage - 1)">&lt;&nbsp;</a>
        <a v-if="searchCondition.currentPage > 1" @click="getPageData(1)">&lt;&lt;&nbsp;</a>

        <template v-for="i in totalPages" :key="i">
          <strong v-if="i === searchCondition.currentPage">{{ i }}</strong>
          <a v-else @click="getPageData(i)">{{ i }}</a>
        </template>

        <a v-if="searchCondition.currentPage < totalPages"
          @click="getPageData(searchCondition.currentPage + 1)">&nbsp;&gt;</a>
        <a v-if="searchCondition.currentPage < totalPages" @click="getPageData(totalPages)">&nbsp;&gt;&gt;</a>

      </div>
      <button @click="clickRegisterButton" class="btn btn-primary">등록</button>

    </div>
  </div>
</template>

<script>
import { api, } from "../scripts/APICreate.js";
import { BOARD_LIST_URL, BOARD_WRITE_URL, BOARD_VIEW_URL } from "../scripts/URLs.js";

export default {

  // 데이터 초기화
  data() {
    return {
      searchCondition: this.createDefaultSearchCondition(),
      searchBoards: [],
      categories: [],
      totalCount: 0,
      totalPages: 0,
    };
  },
  computed: {
    //카테고리 ID에 따른 이름을 가져오는 함수
    getCategoryName() {
      return (categoryId) => {
        const category = this.categories.find((category) => category.categoryId === categoryId);
        return category ? category.categoryName : '';
      };
    },
  },

  mounted() {
    //현재 검색 조건을 만족하는 게시글 리스트 요청 
    this.getBoardList();
  },

  methods: {
    //초기 검색조건을 설정하는 함수 
    createDefaultSearchCondition() {
      const currentDate = new Date();
      const oneYearAgo = new Date(currentDate.getFullYear() - 1, currentDate.getMonth(), currentDate.getDate());
      const formattedStartDate = oneYearAgo.toISOString().slice(0, 10);
      const formattedEndDate = currentDate.toISOString().slice(0, 10);

      const urlParams = new URLSearchParams(window.location.search);
      const defaultCategoryId = urlParams.get('categoryId') || 0;
      const defaultSearchText = urlParams.get('searchText') || '';
      const defaultStartDate = urlParams.get('startDate') || formattedStartDate;
      const defaultEndDate = urlParams.get('endDate') || formattedEndDate;
      const defaultCurrentPage = urlParams.get('currentPage') || 1;
      const defaultPageSize = urlParams.get('pageSize') || 10;
      const defaultOffset = urlParams.get('offset') || 0;

      return {
        categoryId: defaultCategoryId,
        searchText: defaultSearchText,
        startDate: defaultStartDate,
        endDate: defaultEndDate,
        currentPage: defaultCurrentPage,
        pageSize: defaultPageSize,
        offset: defaultOffset,
      };
    },

    //현재 검색조건에 맞는 게시글을 가져오는 함수 
    getBoardList() {

      const params = {
        categoryId: this.searchCondition.categoryId,
        searchText: this.searchCondition.searchText,
        startDate: this.searchCondition.startDate,
        endDate: this.searchCondition.endDate,
        currentPage: this.searchCondition.currentPage,
        pageSize: this.searchCondition.pageSize,
        offset: this.searchCondition.offset,
      };

      const requestURL = `${BOARD_LIST_URL}?${Object.entries(params).map(([key, value]) => `${key}=${value}`).join('&')}`;

      api
        .get(requestURL)
        .then(response => {
          const responseData = response.data.data;

          Object.assign(this.searchCondition, responseData.searchCondition);
          this.totalCount = responseData.totalCount;
          this.totalPages = Math.ceil(responseData.totalCount / this.searchCondition.pageSize);
          this.searchBoards = responseData.searchBoards;
          this.categories = responseData.categories;
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },
    //게시글 등록 버튼 클릭 함수 
    clickRegisterButton() {
      //TODO : searchCondtion으로 코드 줄이기
      //검색 조건과 함께 게시글 정보를 입력할 수 있는 페이지로 이동
      this.$router.push({
        path: BOARD_WRITE_URL,
        query: {
          currentPage: this.searchCondition.currentPage,
          categoryId: this.searchCondition.categoryId,
          searchText: this.searchCondition.searchText,
          startDate: this.searchCondition.startDate,
          endDate: this.searchCondition.endDate,
          pageSize: this.searchCondition.pageSize,
          offset: this.searchCondition.offset,
        }
      });
    },
    //날짜 출력 포맷 변경 함수
    //TODO : 함수설명은 javadoc 스타일로 주석처리
    dateFormat(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    //페이지 번호 이동 시 새로운 검색 조건으로 게시글 정보를 호출하는 함수 
    getPageData(currentPage) {
      this.searchCondition.currentPage = currentPage;
      this.searchCondition.offset = (currentPage - 1) * this.searchCondition.pageSize;
      this.getBoardList();
    },
    //검색 클릭 시 현재 페이지와 오프셋 초기화 후 게시글을 정보를 호출하는 함수
    searchBoardsAction() {
      this.searchCondition.currentPage = 1;
      this.searchCondition.offset = 0;
      this.getBoardList();
    },

    //검색 조건과 함께 게시글 상세 정보 페이지로 이동하는 함수 
    getBoardDetail(boardId, categoryId) {
      const params = {
        boardId: boardId,
        categoryId: categoryId,
        searchText: this.searchCondition.searchText,
        startDate: this.searchCondition.startDate,
        endDate: this.searchCondition.endDate,
        currentPage: this.searchCondition.currentPage,
        pageSize: this.searchCondition.pageSize,
        offset: this.searchCondition.offset,
      };

      return `${BOARD_VIEW_URL}?${Object.entries(params).map(([key, value]) => `${key}=${value}`).join('&')}`;

    }
  },

};
</script>
