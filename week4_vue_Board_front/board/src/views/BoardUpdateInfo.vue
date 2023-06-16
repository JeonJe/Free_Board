<template>
    <div class="container my-5">
        <h1 class="my-4">게시판 - 수정</h1>
        
        <!-- 게시글 내용 -->
        <div class="card mb-3">
            <div class="card-header bg-transparent pb-0">
                <div class="d-flex justify-content-between">
                    <input type="text" id="writer" name="writer" v-model="board.writer" class="form-control" >
                    <input type="password" id="enteredPassword" name="enteredPassword" class="form-control" >
                    <div class="d-flex">
                        <p class="mb-0 me-4 mr-2">등록일시: {{ dateFormat(board.createdAt) }}</p>
                        <p class="mb-0">수정일시: {{ dateFormat(board.modifiedAt) }}</p>
                    </div>
                </div>
                <div class="d-flex justify-content-between pt-2">
                    <h5 class="card-title mb-4">{{ this.board.categoryName }}: 
                    <input type="text" id="title" name="title" v-model="board.title" class="form-control"></h5>
                    <p class="card-text mb-4">조회수: {{ updatedVisitCount }}</p>
                </div>
            </div>
        </div>
        <div class="card-body border">
            <textarea id="content" name="content" v-model="board.content" class="form-control" ></textarea>
        </div>
        <br>
        <div>
            <a v-for="attachment in attachments" :key="attachment.fileName"
                :href="'download?fileName=' + attachment.fileName" class="mb-2 text-decoration-underline d-block">{{
                    attachment.originName }}</a>
        </div>
        <br>
       
        <!-- 버튼그룹 -->
        <div class="d-flex justify-content-center mt-3">
            <div class="buttons">
                <router-link :to="clickCancleButton()" class="btn btn-secondary btn-block">
                    취소
                </router-link>
                 <button @click="updateBoard" class="btn btn-primary btn-block">
                    저장
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import {  BOARD_VIEW_URL } from "../scripts/URLs.js";
import { api, multipartApi, } from "../scripts/APICreate.js";
import moment from 'moment'

export default {
    data() {
        return {
            enteredPassword: '',
            board: {
                boardId: '',
                writer: '',
                password: '',
                createdAt: '',
                modifiedAt: '',
                categoryId: '',
                title: '',
                content: '',
                categoryName: '',
            },
            attachments: [],
            searchCondition: {
                currentPage: '',
                categoryId: '',
                searchText: '',
                startDate: '',
                endDate: '',
                pageSize: 10,
                offset : 0
            },

             formData: {
                categoryId: '',
                writer: '',
                password: '',
                confirmPassword: '',
                title: '',
                content: '',
                files: []
            },

        };
    },
    mounted() {
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
                currentPage: this.searchCondition.currentPage
            };

            const requestURL = `${BOARD_VIEW_URL}?${Object.entries(params).map(([key, value]) => `${key}=${value}`).join('&')}`;
            console.log(requestURL)
            api
                .get(requestURL)
                .then(response => {
                    const responseData = response.data.data;
                    Object.assign(this.board, responseData.board);
                })
                .catch(error => {
                    console.log(error);
                })

        },
      
         getCategoryName(categoryId) {
            // 카테고리 ID를 기반으로 카테고리 이름을 가져오는 로직
            return this.categories.find((category) => category.id === categoryId)?.name || '';
        },

        dateFormat(date) {
            return moment(date).format('YYYY-MM-DD');
        },
        clickCancleButton() {

            return this.getBoardDetail(this.board.boardId);
            
        },
        updateBoard(){  
            
            //TODO : 업데이트 정보를 비밀번호와 함께 서버에 전달 
     
            //  this.$router.push('/board/view'); // 저장 후 이동할 경로 설정
        },
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
            return `${BOARD_VIEW_URL}?${Object.entries(params).map(([key, value]) => `${key}=${value}`).join('&')}`;

        }
         
    },
};
</script>

<style scoped>
.error-message {
    color: red;
}
</style>