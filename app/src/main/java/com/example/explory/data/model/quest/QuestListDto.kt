package com.example.explory.data.model.quest

data class QuestListDto(
    val notCompleted: List<QuestDto>,
    val active: List<QuestDto>,
    val completed: List<CompletedQuestDto>
)
