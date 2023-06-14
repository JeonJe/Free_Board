<template>
  <div class="container">
    <h1 class="my-4">자유 게시판 목록</h1>

    <!-- Form of the Searching Condition -->
    <form class="form-inline mb-4" @submit.prevent="searchBoardsAction">
      <input type="date" id="startDate" name="startDate" class="form-control mr-2" v-model="searchCondition.startDate" />

      <input type="date" id="endDate" name="endDate" class="form-control mr-2" v-model="searchCondition.endDate" />

    <select id="category" name="categoryId" class="form-control mr-2" v-model="searchCondition.categoryId">
    <option value="0">전체 카테고리</option>
    <option v-for="category in categories" :value="category.categoryId" :key="category.categoryId" :selected="category.categoryId === searchCondition.categoryId">
      {{ category.categoryName }}
    </option>
  </select>

      <input type="text" id="searchText" name="searchText" class="form-control mr-2" placeholder="카테고리 + 제목 + 내용"
        v-model="searchCondition.searchText" />

      <button type="submit" class="btn btn-primary">검색</button>
    </form>
  <p>총 {{ this.totalCount }} 건</p>
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
          <td><a :href="'board/view?id=' + boardItem.boardId">{{ boardItem.title }}</a></td>
          <td>{{ boardItem.writer }}</td>
          <td>{{ boardItem.visitCount }}</td>
          <td>{{ dateFormat(boardItem.createdAt) }}</td>
          <td>{{ dateFormat(boardItem.modifiedAt) }}</td>
        </tr>
      </tbody>
    </table>

    <!-- pagination -->
    <div class="d-flex justify-content-between">
      <div class="text-center mx-auto">
         <a v-if="searchCondition.currentPage > 1" @click="getPageData(searchCondition.currentPage - 1)">&lt;&nbsp;</a>
    <a v-if="searchCondition.currentPage > 1" @click="getPageData(1)">&lt;&lt;&nbsp;</a>

    <template v-for="i in totalPages" :key="i">
      <strong v-if="i === searchCondition.currentPage">{{ i }}</strong>
      <a v-else @click="getPageData(i)">{{ i }}</a>
    </template>

    <a v-if="searchCondition.currentPage < totalPages" @click="getPageData(searchCondition.currentPage + 1)">&nbsp;&gt;</a>
    <a v-if="searchCondition.currentPage < totalPages" @click="getPageData(totalPages)">&nbsp;&gt;&gt;</a>
    <!-- ... -->
      </div>
      <button @click="handleRegisterClick" class="btn btn-primary">등록</button>
  
    </div>
  </div>
</template>

<script>
import api from "../scripts/APICreate.js";

export default {
  

  data() {
    const currentDate = new Date();
    const oneYearAgo = new Date(currentDate.getFullYear() - 1, currentDate.getMonth(), currentDate.getDate());
    const formattedStartDate = oneYearAgo.toISOString().slice(0, 10);
    const formattedEndDate = currentDate.toISOString().slice(0, 10);
    //클라이언트가 전달해주는 파라미터 초기값
    return {
      searchCondition: {
        categoryId: 0,
        searchText: '',
        startDate: formattedStartDate,
        endDate: formattedEndDate,
        currentPage: 1,
        pageSize : 10,
        offset : 1,
      },
      searchBoards: [],
      totalCount : 0,
      categories: [],
      categoryName: '',
      totalPages: 0,
    };
  },
  computed: {
    //카테고리 id에 따른 이름을 가져오는 함수
    getCategoryName() {
      return (categoryId) => {
        const category = this.categories.find((category) => category.categoryId === categoryId);
        return category ? category.categoryName : '';
      };
    },
  },

  mounted() {
    this.getBoardList();
  },

  methods: {
    getBoardList() {
      const currentDate = new Date();
      const oneYearAgo = new Date(currentDate.getFullYear() - 1, currentDate.getMonth(), currentDate.getDate());
      const formattedStartDate = oneYearAgo.toISOString().slice(0, 10);
      const formattedEndDate = currentDate.toISOString().slice(0, 10);

      const url = `board/list?currentPage=${this.searchCondition.currentPage}&categoryId=${this.searchCondition.categoryId}&searchText=${this.searchCondition.searchText}&startDate=${this.searchCondition.startDate}&endDate=${this.searchCondition.endDate}&pageSize=${this.searchCondition.pageSize}`;

      api
        .get(url)
        .then(response => {
          const data = response.data;
          const responseData = data.data[0]; 
          
          this.searchCondition.categoryId = responseData.searchCondition.categoryId || 0;
          this.searchCondition.searchText = responseData.searchCondition.searchText || '';
          this.searchCondition.startDate = responseData.searchCondition.startDate || formattedStartDate;
          this.searchCondition.endDate = responseData.searchCondition.endDate || formattedEndDate;
          this.searchCondition.currentPage = responseData.searchCondition.currentPage || 1;
          this.searchCondition.pageSize = responseData.searchCondition.pageSize || 10;
          this.totalCount = responseData.totalCount || 0;
          this.totalPages = Math.ceil(responseData.totalCount / this.searchCondition.pageSize); 
          this.searchBoards = responseData.searchBoards;
          this.categories = responseData.categories;
                    
        })
        .catch(error => {
          console.error('Error:', error);
        });
    },

    handleRegisterClick() {
      this.$router.push({
        path: '/write',
        query: {
          currentPage: this.searchCondition.currentPage,
          categoryId: this.searchCondition.categoryId,
          searchText: this.searchCondition.searchText,
          startDate: this.searchCondition.startDate,
          endDate: this.searchCondition.endDate
        }
      });
    },
    dateFormat(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    getPageData(currentPage) {
      this.searchCondition.currentPage = currentPage; 
      this.getBoardList();
    },
    searchBoardsAction() {
      this.searchCondition.currentPage = 1;
      this.getBoardList();
    },
  },
  
};
</script>
