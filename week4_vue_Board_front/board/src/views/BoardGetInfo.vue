<template>
    <div class="container my-5">
        <h1 class="my-4">게시판 - 보기</h1>
        <!-- 비밀번호 확인 모달  -->
        <div v-if="showPasswordModal" class="modal">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">비밀번호 확인</h5>
                        <button type="button" class="close" data-dismiss="modal"
                            @click="hidePasswordModal()">&times;</button>
                    </div>
                    <div class="modal-body">
                        <form @submit="validatePassword">
                            <input type="password" v-model="password" required>
                            <br>
                            <div v-if="passwordError" class="error-message">{{ passwordError }}</div>
                            <br>
                            <input type="submit" value="확인" class="btn btn-primary">
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
                      <h5 class="card-title mb-4">{{ this.board.categoryName }}: {{ board.title }}</h5>
                    <p class="card-text mb-4">조회수: {{ updatedVisitCount }}</p>
                </div>
            </div>
        </div>
        <div class="card-body border">
            <div class="card-text">{{ board.content }}</div>
        </div>
        <br>
        <div>
            <a v-for="attachment in attachments" :key="attachment.fileName"
                :href="'download?fileName=' + attachment.fileName" class="mb-2 text-decoration-underline d-block">{{
                    attachment.originName }}</a>
        </div>
        <br>
        <!-- 댓글 -->
        <div>
            <div v-if="comments && comments.length > 0" class="list-group comment-item bg-light">
                <div v-for="comment in comments" :key="comment.createdAt" class="list-group-item comment-item">
                    <div class="d-flex justify-content-between">
                        <small class="mb-1">{{ comment.createdAt }}</small>
                    </div>
                    <p class="mb-1">{{ comment.content }}</p>
                </div>
            </div>
            <p v-else>댓글이 없습니다.</p>
        </div>
        <!-- 댓글 추가 -->
        <div class="d-flex justify-content-center my-3">
            <form @submit="addComment" class="w-75">
                <div class="row">
                    <div class="col-8">
                        <div class="form-group">
                            <textarea class="form-control" v-model="commentContent" rows="4"
                                placeholder="댓글을 입력해주세요."></textarea>
                        </div>
                    </div>
                    <div class="col-4 d-flex align-items-center">
                        <button class="btn btn-primary" type="submit">댓글 등록</button>
                    </div>
                </div>
            </form>
        </div>
        <!-- 버튼그룹 -->
        <div class="d-flex justify-content-center mt-3">
            <div class="buttons">
                <!-- <a :href="'list?action=list&page=' + param.page + '&category=' + param.category + '&searchText=' + param.searchText + '&startDate=' + param.startDate + '&endDate=' + param.endDate"
                    class="btn btn-secondary">목록으로 돌아가기</a> -->
                <button class="btn btn-primary" @click="handlePasswordModal('edit')">수정</button>
                <button class="btn btn-primary" @click="handlePasswordModal('delete')">삭제</button>
            </div>
        </div>
</div>
</template>

<script>
import { api, } from "../scripts/APICreate.js";
import moment from 'moment'

export default {
    data() {
        return {
            showPasswordModal: false,
            isEdit: false,
            password: '',
            passwordError: '',
            board: {
                boardId: '',
                writer: '', 
                createdAt: '',
                modifiedAt: '', 
                categoryId: '', 
                title: '', 
                content: '', 
                categoryName: '',
            },
            updatedVisitCount: 0, 
            attachments: [], 
            comments: [], 
            commentContent: '', 
            searchCondition: {
                currentPage: '',
                categoryId: '',
                searchText: '',
                startDate: '',
                endDate: '',
            },
        };
    },
    mounted() {
        this.searchCondition.currentPage = this.$route.query.currentPage;
        this.searchCondition.categoryId = this.$route.query.categoryId;
        this.searchCondition.searchText = this.$route.query.searchText;
        this.searchCondition.startDate = this.$route.query.startDate;
        this.searchCondition.endDate = this.$route.query.endDate;
        this.board.boardId = this.$route.query.boardId;
        
        // 게시글 정보를 가져오는 API 호출
        this.fetchBoardData();
        
    },
    methods: {
        fetchBoardData() {
            // 게시글 정보를 가져오는 API 호출 및 데이터 할당
            const url = `board/view?boardId=${this.board.boardId}&categoryId=${this.searchCondition.categoryId}&searchText=${this.searchCondition.searchText}&startDate=${this.searchCondition.startDate}&endDate=${this.searchCondition.endDate}&currentPage=${this.searchCondition.currentPage}`;
            api
                .get(url)
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
            // 예시:
            return this.categories.find((category) => category.id === categoryId)?.name || '';
        },
        dateFormat(date) {
            // 날짜 포맷 변환 로직
            // 예시:
            return moment(date).format('YYYY-MM-DD');
        },
        hidePasswordModal() {
            this.showPasswordModal = false;
        },
        handlePasswordModal(action) {
            this.showPasswordModal = true;
            this.isEdit = action === 'edit';
        },
        async validatePassword() {
            // const enteredPassword = this.password;
            const hashedPassword = this.$CryptoJS.SHA256(this.enteredPassword).toString();
            // 비밀번호 해싱 로직 (예: CryptoJS.SHA256(enteredPassword).toString())

      // 비밀번호 검증 API 호출
      // 호출 결과에 따라 처리

        if (hashedPassword === this.password) {
                    this.hidePasswordModal();
                    if (this.isEdit) {
                        // 수정 페이지로 이동하는 로직
                        // 예시:
                        // const redirectURL = `modify?action=modify&id=${this.board.boardId}&page=${this.param.page}&category=${this.param.category}&search=${this.param.searchText}&startDate=${this.param.startDate}&endDate=${this.param.endDate}`;
                        // window.location.replace(redirectURL);
                    } else {
                        try {
                            // 삭제 API 호출
                            // 호출 결과에 따라 처리
                            // 예시:
                            // const response = await axios.post('/api/board/delete', {
                            //   id: this.board.boardId,
                            //   password: enteredPassword,
                            // });
                            // if (!response.ok) {
                            //   alert('삭제에 실패하였습니다');
                            // }
                            // window.location.replace('list');
                        } catch (error) {
                            alert(error);
                        }
                    }
                } else {
                    this.passwordError = '비밀번호가 일치하지 않습니다.';
                }
        },
        addComment() {
            // 새로운 댓글 등록 로직
            // API 호출 예시: axios.post('/api/comment/add', { content: this.commentContent, boardId: this.board.boardId })
            // 호출 결과에 따라 처리
            // 댓글 등록 후, 새로운 댓글 목록을 가져와서 this.comments에 할당
            // 예시:
            // this.commentContent = '';
            // const response = await axios.post('/api/comment/list', { boardId: this.board.boardId });
            // this.comments = response.data.comments;
        },
    },
};
</script>

<style scoped>
.error-message {
    color: red;
}
</style>